package mk.ukim.finki.wbs.mapper;

import mk.ukim.finki.wbs.model.AnswerModel;
import mk.ukim.finki.wbs.model.QuestionModel;
import mk.ukim.finki.wbs.model.UserModel;
import mk.ukim.finki.wbs.model.jsonld.User;
import mk.ukim.finki.wbs.repository.AnswerRepository;
import mk.ukim.finki.wbs.repository.QuestionRepository;
import mk.ukim.finki.wbs.repository.UserRepository;
import org.apache.jena.rdf.model.Resource;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JsonLDToModelMapper {

    private QuestionModel questionModel;
    private AnswerModel answerModel;
    private UserModel userModel;
    private QuestionRepository questionRepository;
    private AnswerRepository answerRepository;
    private UserRepository userRepository;

    public JsonLDToModelMapper(QuestionModel questionModel, AnswerModel answerModel, UserModel userModel, QuestionRepository questionRepository, AnswerRepository answerRepository, UserRepository userRepository) {
        this.questionModel = questionModel;
        this.answerModel = answerModel;
        this.userModel = userModel;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
    }

    public Resource getUserResource(User user) {
        return this.userRepository.getUser(user.getId()).orElseThrow(IllegalArgumentException::new);
    }
}
