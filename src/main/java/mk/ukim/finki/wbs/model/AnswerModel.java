package mk.ukim.finki.wbs.model;

import com.ibm.icu.text.Transliterator;
import lombok.Data;
import mk.ukim.finki.wbs.client.FusekiClient;
import mk.ukim.finki.wbs.model.enumeration.PropertyType;
import org.apache.jena.rdf.model.*;
import org.apache.jena.rdf.model.impl.PropertyImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Data
@Component
public class AnswerModel {

    private Model model;

    private Map<String, String> prefixes;

    private FusekiClient fusekiClient;

    private Property title;
    private Property explanation;
    private Property writer;
    private Property likedBy;
    private Property dislikedBy;
    private Property replyOf;
    private Property date;
    private Property prefix;
    private Property uri;

//    @Autowired
//    public void configureFusekiClient(@Value("${fuseki.endpoint}") String baseUrl, @Value("${fuseki.dataset}") String dataset) {
//        fusekiClient = new FusekiClient(baseUrl + dataset);
//        this.model = fusekiClient.getAnswerModel();
//        this.prefixes = fusekiClient.getPrefixes();
//        this.createProperties();
//    }

    public AnswerModel(FusekiClient fusekiClient) {
        this.fusekiClient = fusekiClient;
        this.model = fusekiClient.getAnswerModel();
        this.prefixes = fusekiClient.getPrefixes();
        this.createProperties();
    }

    private void createProperties() {
        this.title = new PropertyImpl(this.prefixes.get("dbp").concat(PropertyType.title.getType()));
        this.explanation = new PropertyImpl(this.prefixes.get("dbp").concat(PropertyType.explanation.getType()));
        this.writer = new PropertyImpl(this.prefixes.get("dbp").concat(PropertyType.writer.getType()));
        this.likedBy = new PropertyImpl(this.prefixes.get("sor").concat(PropertyType.likedBy.getType()));
        this.dislikedBy = new PropertyImpl(this.prefixes.get("sor").concat(PropertyType.dislikedBy.getType()));
        this.replyOf = new PropertyImpl(this.prefixes.get("sioc").concat(PropertyType.replyOf.getType()));
        this.date = new PropertyImpl(this.prefixes.get("dbp").concat(PropertyType.date.getType()));
        this.prefix = new PropertyImpl(this.prefixes.get("dbp").concat(PropertyType.prefix.getType()));
        this.uri = new PropertyImpl(this.prefixes.get("dbp").concat(PropertyType.uri.getType()));
    }

    public Map<Property, List<RDFNode>> getPropertiesMap(Resource resource) {
        return resource.listProperties()
                .toList()
                .stream()
                .collect(Collectors.groupingBy(Statement::getPredicate,
                        Collectors.mapping(Statement::getObject, Collectors.toList())));

    }

}
