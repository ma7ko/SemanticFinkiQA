package mk.ukim.finki.wbs.client;
import mk.ukim.finki.wbs.model.enumeration.ResourceType;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.rdfconnection.RDFConnectionFuseki;
import org.apache.jena.rdf.model.*;
import org.apache.jena.rdf.model.impl.PropertyImpl;
import org.apache.jena.rdf.model.impl.ResourceImpl;
import org.apache.jena.rdf.model.impl.StatementImpl;

import org.apache.jena.update.Update;
import org.apache.jena.update.UpdateRequest;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class FusekiClient
{
    private String queryService;
    private RDFConnectionFuseki rdfConnection;
    private Map<String, String> prefixes;
    private Model model;


    public FusekiClient(@Value("${fuseki.endpoint}") String baseUrl, @Value("${fuseki.dataset}") String dataset) {
        this.queryService = baseUrl.concat(dataset);
        this.rdfConnection = RDFConnectionFactory.connectFuseki(this.queryService);
        this.model = rdfConnection.fetch();
        this.prefixes = this.model.getNsPrefixMap();
    }


    //INCLUDE
    public List<Statement> executeGetQuery(final String subj, final String pred, final String obj) {

        Resource subject = (subj == null) ? null : new ResourceImpl(subj);
        Property predicate = (pred == null) ? null : new PropertyImpl(pred);
        RDFNode object = (obj == null) ? null : new ResourceImpl(obj);

        Selector selector = new SimpleSelector(subject, predicate, object);

        List<Statement> statements = this.model.listStatements(selector).toList();

        Resource r = this.model.getResource(subj);

        Resource newr = this.model.getResource("http://dbpedia.org/resource/Question");
        List<Property> listproperty = newr.listProperties().toList().stream().map(pr -> pr.getPredicate()).collect(Collectors.toList());
        Selector selector1 = new SimpleSelector(null, new PropertyImpl(prefixes.get("rdfs")+"domain"), new ResourceImpl(prefixes.get("dbr")+ "Question"));
        List<Resource> newM = this.model.listResourcesWithProperty(new PropertyImpl(prefixes.get("rdfs")+"domain"), new ResourceImpl(prefixes.get("dbr")+ "Question")).toList();

        String res = r.toString();

        Model m = this.model.listStatements(selector).toModel();

        List<String> li = m.listObjects().toList().stream().map(i -> i.toString()).collect(Collectors.toList());

        return statements;

    }

    public Resource executeCreateQuery(String URI) {
        return this.model.createResource(URI);
    }

    //INCLUDE
    // This update method is specifically for question cause it contains questions properties, should
    // be either in question repository or make it general
    public Resource executeUpdateQuery(Resource resource, String resourceType) {
        //one method
        Statement stmt = new StatementImpl(resource, RDF.type, new ResourceImpl(resourceType));
        this.model.add(stmt);

        this.rdfConnection.put(this.model);
        this.rdfConnection.commit();
        //end of method
        return resource;
    }

    public Statement createStatement(Resource resource, Property property, RDFNode rdfNode) {
        return this.model.createStatement(resource, property,rdfNode);
    }

    public Boolean executeSingleUpdateQuery(Statement statement, Statement statement2) {
        List<Statement> statementList = new ArrayList<>();
        statementList.add(statement);
        statementList.add(statement2);
        this.model.add(statementList);
        this.rdfConnection.put(this.model);
        return true;
    }


    //INCLUDE

    public Resource executeDeleteQuery(String resourceURI) {

        Resource resource = this.model.getResource(resourceURI);
        resource = resource.removeProperties();
        List<Statement> statements = this.executeGetQuery(resourceURI, null ,null);
        statements.addAll(this.executeGetQuery(null, null, resourceURI));

        this.model.remove(statements);


        this.rdfConnection.put(this.model);

        return resource;
    }

    public void executeSingleDeleteQuery(Statement statement) {
        this.model.remove(statement);
        this.rdfConnection.put(this.model);
    }

    public Map<String, String> getPrefixes() {
        return prefixes;
    }

    public Model getQuestionModel() {
        Model questionModel = this.model.listStatements(null,
                RDF.type,
                new ResourceImpl(prefixes.get("dbr").concat(ResourceType.question.getType())))
                .toModel();

        return questionModel;
    }

    public Model getAnswerModel() {
        Model answerModel = this.model.listStatements(null,
                RDF.type,
                new ResourceImpl(prefixes.get("dbr").concat(ResourceType.answer.getType())))
                .toModel();

        return answerModel;
    }

    public Model getUserModel() {
        Model userModel = this.model.listStatements(null,
                RDF.type,
                new ResourceImpl(prefixes.get("dbr").concat(ResourceType.user.getType())))
                .toModel();

        return userModel;
    }

    public void addResourceToModel(Resource resource, String resourceType) {
        Statement stmt = new StatementImpl(resource, RDF.type, new ResourceImpl(resourceType));
        this.model.add(stmt);
    }

}