package mk.ukim.finki.wbs.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.text.Transliterator;
import ioinformarics.oss.jackson.module.jsonld.JsonldModule;
import mk.ukim.finki.wbs.model.QuestionModel;
import mk.ukim.finki.wbs.model.dto.QuestionDto;
import mk.ukim.finki.wbs.model.jsonld.Question;
import mk.ukim.finki.wbs.model.jsonld.User;
import mk.ukim.finki.wbs.repository.QuestionRepository;
import mk.ukim.finki.wbs.repository.UserRepository;
import mk.ukim.finki.wbs.service.QuestionService;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionModel questionModel;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository,
                               UserRepository userRepository,
                               QuestionModel questionModel) {
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.questionModel = questionModel;
    }

    @Override
    public List<Resource> getQuestions() {
        Comparator<Resource> dateComparator = (c1, c2) -> {
            String date1 = c1.getProperty(this.questionModel.getDate()).getObject().toString();
            String date2 = c2.getProperty(this.questionModel.getDate()).getObject().toString();
            LocalDateTime date1o = LocalDateTime.parse(date1);
            LocalDateTime date2o = LocalDateTime.parse(date2);
            if (date1o.isBefore(date2o)) return 1;
            else return -1;
        };
        return this.questionRepository.listQuestions().stream().sorted(dateComparator).collect(Collectors.toList());
    }

    @Override
    public Optional<Statement> getQuestion(String URI) {
       return this.questionRepository.getQuestion(URI);
    }

    @Override
    public Optional<Resource> addQuestion(String uri, String prefix, String title, String description, String userURI, List<String> tags) {

        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException();
        }

        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException();
        }

        if (userURI == null || userURI.isEmpty()) {
            throw new IllegalArgumentException();
        }

        if (tags == null) {
            throw new IllegalArgumentException();
        }

        String currentUri = questionModel.getURIFromTitle(title);

        if (uri != null) {
            Optional<Statement> statement = this.getQuestion(prefix.concat(uri));

            if (statement.isPresent()) {
                Resource question = statement.get().getSubject();
                this.deleteQuestion(question.toString());
            }
        }

        Resource question = this.questionRepository
                    .createQuestion(prefix, currentUri)
                    .orElseThrow(IllegalArgumentException::new);


        Resource user = this.userRepository.getUser(userURI).orElseThrow(IllegalArgumentException::new);


        question.addProperty(this.questionModel.getTitle(), title)
                .addProperty(this.questionModel.getDescription(), description)
                .addProperty(this.questionModel.getWriter(), user)
                .addProperty(this.questionModel.getPrefix(), prefix)
                .addProperty(this.questionModel.getUri(), currentUri)
                .addProperty(this.questionModel.getDate(), LocalDateTime.now().toString());

        tags.forEach(tag -> {
            question.addProperty(this.questionModel.getTag(), tag);
        });

        return this.questionRepository.saveQuestion(question);
    }

    @Override
    public void deleteQuestion(String questionURI) {
        this.questionRepository.deleteQuestion(questionURI);

    }

    @Override
    public Optional<Boolean> likeQuestion(String questionId, String userId) {
        Resource question = this.questionRepository.getQuestion(questionId).get().getSubject();
        Resource user = this.userRepository.getUser(userId).orElseThrow(IllegalArgumentException::new);

        Optional<Statement> alreadyLikedStatement = this.questionRepository.getSingleStatement(question.toString(), questionModel.getLikedBy().toString(), user.toString());

        if (alreadyLikedStatement.isPresent()) {
            this.questionRepository.deleteQuestionReaction(alreadyLikedStatement.get());
        } else {
            question.addProperty(questionModel.getLikedBy(), user);
        }
        Optional<Resource> savedQuestion = this.questionRepository.saveQuestion(question);

        if (savedQuestion.isEmpty()) {
            return Optional.of(false);
        }
        return Optional.of(true);
    }

    @Override
    public Optional<Boolean> dislikeQuestion(String questionId, String userId) {
        Resource question = this.questionRepository.getQuestion(questionId).get().getSubject();
        Resource user = this.userRepository.getUser(userId).orElseThrow(IllegalArgumentException::new);

        Optional<Statement> alreadyDislikedStatement = this.questionRepository.getSingleStatement(question.toString(), questionModel.getDislikedBy().toString(), user.toString());

        if (alreadyDislikedStatement.isPresent()) {
            this.questionRepository.deleteQuestionReaction(alreadyDislikedStatement.get());
        } else {
            question.addProperty(questionModel.getDislikedBy(), user);
        }
        Optional<Resource> q = this.questionRepository.saveQuestion(question);

        if (q.isEmpty()) {
            return Optional.of(false);
        }
        return Optional.of(true);
    }

}
