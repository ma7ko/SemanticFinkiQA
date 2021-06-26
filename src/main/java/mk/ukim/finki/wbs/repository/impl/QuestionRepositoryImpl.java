package mk.ukim.finki.wbs.repository.impl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.icu.text.Transliterator;
import mk.ukim.finki.wbs.client.FusekiClient;
import mk.ukim.finki.wbs.mapper.ModelToJsonLDMapper;
import mk.ukim.finki.wbs.model.QuestionModel;
import mk.ukim.finki.wbs.model.dto.AnswerDto;
import mk.ukim.finki.wbs.model.dto.QuestionDto;
import mk.ukim.finki.wbs.model.enumeration.ResourceType;
import mk.ukim.finki.wbs.repository.AnswerRepository;
import mk.ukim.finki.wbs.repository.QuestionRepository;
import org.apache.jena.rdf.model.*;
import org.apache.jena.rdf.model.impl.ResourceImpl;
import org.apache.jena.rdf.model.impl.StatementImpl;
import org.apache.jena.vocabulary.RDF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Repository
public class QuestionRepositoryImpl implements QuestionRepository {
    private FusekiClient fusekiClient;
    private AnswerRepository answerRepository;
    private QuestionModel  questionModel;
    private ModelToJsonLDMapper mapper;
    private Map<String, String> prefixes;

//    @Autowired
//    public void configureFusekiClient(@Value("${fuseki.endpoint}") String baseUrl, @Value("${fuseki.dataset}") String dataset) {
//        fusekiClient = new FusekiClient(baseUrl + dataset);
//        this.prefixes = this.fusekiClient.getPrefixes();
//    }


    public QuestionRepositoryImpl(FusekiClient fusekiClient, AnswerRepository answerRepository, QuestionModel questionModel, ModelToJsonLDMapper mapper) {
        this.fusekiClient = fusekiClient;
        this.answerRepository = answerRepository;
        this.questionModel = questionModel;
        this.mapper = mapper;
        this.prefixes = this.fusekiClient.getPrefixes();
    }

    //INCLUDE
    @Override
    public List<Resource> listQuestions() {
        List<Resource> quests = new ArrayList<>();
        List<Statement> filteredStatements = fusekiClient.executeGetQuery(null, RDF.type.toString(), this.prefixes.get("dbr")+ ResourceType.question.getType());

        filteredStatements.forEach(statement -> {
            Resource question = statement.getSubject();
            quests.add(question);
        });

        return quests;
    }

    //INCLUDE
    @Override
    public Optional<Statement> getQuestion(String URI) {

        if (URI == null || URI.isEmpty()) {
            throw new IllegalArgumentException();
        }
        Optional<Statement> statement = fusekiClient.executeGetQuery(URI, null, null).stream().findFirst();

        return statement;
    }

    @Override
    public Optional<Resource> createQuestion(String prefix, String URI) {
        return (prefix == null) ?
                Optional.of(this.fusekiClient.executeCreateQuery(this.prefixes.get("qst").concat(URI))) :
                Optional.of(this.fusekiClient.executeCreateQuery(prefix.concat(URI)));
    }


    //INCLUDE
    @Override
    public Optional<Resource> saveQuestion(Resource question) {
        return Optional.of(fusekiClient.executeUpdateQuery(question, this.prefixes.get("dbr")+"Question"));
    }

    @Override
    public void updateQuestion(Resource resource, Property property, RDFNode rdfNode) {
        Statement updatedResource = this.fusekiClient.createStatement(resource, property, rdfNode);
        Statement savedAnswer = new StatementImpl(rdfNode.asResource(), RDF.type, new ResourceImpl(this.prefixes.get("dbr").concat("Answer")));
        this.fusekiClient.executeSingleUpdateQuery(updatedResource, savedAnswer);

    }

    //INCLUDE
    @Override
    public void deleteQuestion(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException();
        }
        fusekiClient.executeDeleteQuery(name);
    }

    @Override
    public void deleteQuestionReaction(Statement statement) {
        if (statement == null) {
            throw new IllegalArgumentException();
        }
        this.fusekiClient.executeSingleDeleteQuery(statement);
    }

    @Override
    public Optional<Statement> getSingleStatement(String subj, String pred, String obj) {
        return this.fusekiClient.executeGetQuery(subj, pred, obj).stream().findFirst();
    }


}
