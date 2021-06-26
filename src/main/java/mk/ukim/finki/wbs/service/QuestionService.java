package mk.ukim.finki.wbs.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import mk.ukim.finki.wbs.model.dto.QuestionDto;
import mk.ukim.finki.wbs.model.jsonld.User;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

import java.util.List;
import java.util.Optional;

public interface QuestionService {
    List<Resource> getQuestions();
    Optional<Statement> getQuestion(String URI) throws JsonProcessingException;
    Optional<Resource> addQuestion(String uri, String prefix, String title, String description, String userURI, List<String> tags);
    void deleteQuestion(String questionURI);
    Optional<Boolean> likeQuestion(String questionId, String userId);
    Optional<Boolean> dislikeQuestion(String questionId, String userId);
}
