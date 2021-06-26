package mk.ukim.finki.wbs.repository;

import mk.ukim.finki.wbs.model.dto.AnswerDto;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository {
    List<Resource> getAnswersByQuestion(Resource question);
    Optional<Statement> getAnswer(String URI);
    Optional<Resource> createAnswer(String prefix, String URI);
    Optional<Resource> saveAnswer(Resource answer);
    void addSingleAdd(Resource answer);
    void deleteAnswer(String URI);
    void deleteAnswerReaction(Statement statement);
    Optional<Statement> getSingleStatement(String subj, String pred, String obj);
}
