package mk.ukim.finki.wbs.repository.impl;
import mk.ukim.finki.wbs.client.FusekiClient;
import mk.ukim.finki.wbs.model.AnswerModel;
import mk.ukim.finki.wbs.model.dto.AnswerDto;
import mk.ukim.finki.wbs.repository.AnswerRepository;
import org.apache.jena.rdf.model.*;
import org.apache.jena.rdf.model.impl.ResourceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class AnswerRepositoryImpl implements AnswerRepository {

    private FusekiClient fusekiClient;
    private Map<String, String> prefixes;
    private AnswerModel answerModel;


//    @Autowired
//    public void configureFusekiClient(@Value("${fuseki.endpoint}") String baseUrl, @Value("${fuseki.dataset}") String dataset) {
//        this.fusekiClient = new FusekiClient(baseUrl + dataset);
//        this.prefixes = this.fusekiClient.getPrefixes();
//    }

    public AnswerRepositoryImpl(FusekiClient fusekiClient, AnswerModel answerModel) {
        this.fusekiClient = fusekiClient;
        this.prefixes = this.fusekiClient.getPrefixes();
        this.answerModel = answerModel;
    }

    //INCLUDE
    @Override
    public List<Resource> getAnswersByQuestion(Resource question) {

        List<Statement> statements = this.fusekiClient.executeGetQuery(null, answerModel.getReplyOf().toString(), question.toString());
        List<Resource> answers = new ArrayList<>();

        statements.forEach(statement -> {

            Resource answer = statement.getSubject();
            answers.add(answer);

        });

        return answers;
    }


    @Override
    public Optional<Statement> getAnswer(String URI) {
        Optional<Statement> statement = fusekiClient.executeGetQuery(URI, null, null).stream().findFirst();

        return statement;
    }

    @Override
    public Optional<Resource> createAnswer(String prefix, String URI) {
        return (prefix == null) ?
                Optional.of(this.fusekiClient.executeCreateQuery(this.prefixes.get("awr").concat(URI))) :
                Optional.of(this.fusekiClient.executeCreateQuery(prefix.concat(URI)));
    }

    @Override
    public Optional<Resource> saveAnswer(Resource answer) {
        return Optional.of(this.fusekiClient.executeUpdateQuery(answer, this.prefixes.get("dbr")+"Answer"));
    }

    @Override
    public void addSingleAdd(Resource answer) {
        this.fusekiClient.addResourceToModel(answer, this.prefixes.get("dbr")+"Answer");
    }

    @Override
    public void deleteAnswer(String URI) {
        fusekiClient.executeDeleteQuery(URI);
    }

    @Override
    public void deleteAnswerReaction(Statement statement) {
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
