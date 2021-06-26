package mk.ukim.finki.wbs.repository;

import mk.ukim.finki.wbs.model.dto.QuestionDto;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository {
    List<Resource> listQuestions();
    Optional<Statement> getQuestion(String URI);
    Optional<Resource> createQuestion(String prefix, String URI);
    Optional<Resource> saveQuestion(Resource question);
    void updateQuestion(Resource resource, Property property, RDFNode rdfNode);
    void deleteQuestion(String URI);
    void deleteQuestionReaction(Statement statement);
    Optional<Statement> getSingleStatement(String subj, String pred, String obj);

}
