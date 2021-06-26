package mk.ukim.finki.wbs.service;

import mk.ukim.finki.wbs.model.dto.AnswerDto;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

import java.util.List;
import java.util.Optional;

public interface AnswerService {
    List<Resource> getAnswersByQuestion(String URI);
    Optional<Statement> getAnswer(String URI);
    Optional<Resource> addAnswer(String prefix, String explanation, String userURI, String questionURI);
    void deleteAnswer(String answerURI);
    Optional<Boolean> likeAnswer(String questionId, String userId);
    Optional<Boolean> dislikeAnswer(String questionId, String userId);
}
