package mk.ukim.finki.wbs.service.impl;

import mk.ukim.finki.wbs.model.AnswerModel;
import mk.ukim.finki.wbs.model.QuestionModel;
import mk.ukim.finki.wbs.model.UserModel;
import mk.ukim.finki.wbs.model.dto.AnswerDto;
import mk.ukim.finki.wbs.model.jsonld.User;
import mk.ukim.finki.wbs.repository.AnswerRepository;
import mk.ukim.finki.wbs.repository.QuestionRepository;
import mk.ukim.finki.wbs.repository.UserRepository;
import mk.ukim.finki.wbs.service.AnswerService;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.impl.ResourceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final AnswerModel answerModel;
    private final QuestionModel questionModel;
    private final UserModel userModel;

    public AnswerServiceImpl(AnswerRepository answerRepository,
                             QuestionRepository questionRepository,
                             UserRepository userRepository,
                             AnswerModel answerModel,
                             QuestionModel questionModel,
                             UserModel userModel) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.answerModel = answerModel;
        this.questionModel = questionModel;
        this.userModel = userModel;
    }

    @Override
    public List<Resource> getAnswersByQuestion(String URI) {
        Optional<Statement> statement = this.questionRepository.getQuestion(URI);

        if (statement.isEmpty())
            throw new IllegalArgumentException();

        Resource question = statement.get().getSubject();

        return this.answerRepository.getAnswersByQuestion(question);
    }

    @Override
    public Optional<Statement> getAnswer(String URI) {
        return this.answerRepository.getAnswer(URI);
    }

    @Override
    public Optional<Resource> addAnswer(String prefix, String explanation, String userURI, String questionURI) {

        if (explanation == null || explanation.isEmpty()) {
            throw new IllegalArgumentException();
        }

        if (userURI == null || userURI.isEmpty()) {
            throw new IllegalArgumentException();
        }

        if (questionURI == null || questionURI.isEmpty()) {
            throw new IllegalArgumentException();
        }

        String quest_uri = questionURI.split("/")[questionURI.split("/").length-1];
        String usr_uri = userURI.split("/")[userURI.split("/").length-1];

        String uri = quest_uri.concat("-").concat(usr_uri);

        Optional<Statement> statement = this.getAnswer(prefix.concat(uri));

        Resource answer;

        if (statement.isPresent()) {
            answer = statement.get().getSubject();
            answer.removeProperties();
        } else {
            answer = this.answerRepository
                    .createAnswer(prefix, uri)
                    .orElseThrow(IllegalArgumentException::new);
        }


        Resource question = this.questionRepository.getQuestion(questionURI).get().getSubject();
        Resource user = this.userRepository.getUser(userURI).orElseThrow(IllegalArgumentException::new);

        answer.addProperty(this.answerModel.getExplanation(), explanation)
                .addProperty(this.answerModel.getWriter(), user)
                .addProperty(this.answerModel.getDate(), LocalDateTime.now().toString())
                .addProperty(this.answerModel.getReplyOf(), question)
                .addProperty(this.answerModel.getPrefix(), (prefix == null) ? this.answerModel.getPrefixes().get("awr") : prefix)
                .addProperty(this.answerModel.getUri(), uri);

        Optional<Resource> savedAnswer = this.answerRepository.saveAnswer(answer);

        return savedAnswer;
    }

    @Override
    public void deleteAnswer(String answerURI) {
        this.answerRepository.deleteAnswer(answerURI);
    }

    @Override
    public Optional<Boolean> likeAnswer(String questionId, String userId) {
        Resource answer = this.answerRepository.getAnswer(questionId).orElseThrow(IllegalArgumentException::new).getSubject();
        Resource user = this.userRepository.getUser(userId).orElseThrow(IllegalArgumentException::new);
        RDFNode questionRef = answer.getProperty(answerModel.getReplyOf()).getObject();
        Resource question = this.questionRepository.getQuestion(questionRef.asResource().toString()).get().getSubject();

        Optional<Statement> alreadyLikedStatement = this.answerRepository.getSingleStatement(answer.toString(), answerModel.getLikedBy().toString(), user.toString());

        if (alreadyLikedStatement.isPresent()) {
            this.answerRepository.deleteAnswerReaction(alreadyLikedStatement.get());
        } else {
            answer.addProperty(answerModel.getLikedBy(), user);
            Optional<Resource> q = this.answerRepository.saveAnswer(answer);

            if (q.isEmpty()) {
                return Optional.of(false);
            }
        }

        question.addProperty(this.questionModel.getHasReply(), answer);
        this.questionRepository.saveQuestion(question);

        return Optional.of(true);
    }

    @Override
    public Optional<Boolean> dislikeAnswer(String questionId, String userId) {
        Resource answer = this.answerRepository.getAnswer(questionId).orElseThrow(IllegalArgumentException::new).getSubject();
        Resource user = this.userRepository.getUser(userId).orElseThrow(IllegalArgumentException::new);

        Optional<Statement> alreadyDislikedStatement = this.answerRepository.getSingleStatement(answer.toString(), answerModel.getDislikedBy().toString(), user.toString());

        if (alreadyDislikedStatement.isPresent()) {
            this.answerRepository.deleteAnswerReaction(alreadyDislikedStatement.get());
        } else {
            answer.addProperty(answerModel.getDislikedBy(), user);
            Optional<Resource> q = this.answerRepository.saveAnswer(answer);

            if (q.isEmpty()) {
                return Optional.of(false);
            }
        }

        return Optional.of(true);
    }
}
