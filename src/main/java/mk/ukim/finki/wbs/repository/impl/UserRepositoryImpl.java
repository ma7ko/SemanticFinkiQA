package mk.ukim.finki.wbs.repository.impl;

import mk.ukim.finki.wbs.client.FusekiClient;
import mk.ukim.finki.wbs.model.UserModel;
import mk.ukim.finki.wbs.model.enumeration.ResourceType;
import mk.ukim.finki.wbs.repository.UserRepository;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.RDF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private FusekiClient fusekiClient;
    private Map<String, String> prefixes;
    private UserModel userModel;

//    @Autowired
//    public void configureFusekiClient(@Value("${fuseki.endpoint}") String baseUrl, @Value("${fuseki.dataset}") String dataset) {
//        fusekiClient = new FusekiClient(baseUrl + dataset);
//        this.prefixes = this.fusekiClient.getPrefixes();
//    }

    public UserRepositoryImpl(FusekiClient fusekiClient,
                              UserModel userModel) {
        this.userModel = userModel;
        this.fusekiClient = fusekiClient;
        this.prefixes = this.fusekiClient.getPrefixes();
    }

    @Override
    public List<Resource> listUsers() {
        List<Statement> statements = this.fusekiClient.executeGetQuery(null, RDF.type.toString(), this.prefixes.get("dbr").concat(ResourceType.user.getType()));
        return statements.stream().map(s -> s.getSubject()).collect(Collectors.toList());
    }

    @Override
    public Optional<Resource> getUser(String URI) {
        if (URI == null || URI.isEmpty()) {
            throw new IllegalArgumentException();
        }
        Optional<Statement> statement = fusekiClient.executeGetQuery(URI, null, null).stream().findFirst();

        if (statement.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return Optional.of(statement.get().getSubject());
    }

    @Override
    public Optional<Resource> createUser(String prefix, String URI) {
        return (prefix == null) ?
                Optional.of(this.fusekiClient.executeCreateQuery(this.prefixes.get("usr").concat(URI))) :
                Optional.of(this.fusekiClient.executeCreateQuery(prefix.concat(URI)));
    }

    @Override
    public Optional<Resource> saveUser(Resource resource) {
        return Optional.of(fusekiClient.executeUpdateQuery(resource, this.prefixes.get("dbr")+"User"));
    }

    @Override
    public Optional<Resource> getUserByUsername(String username) {
        List<Resource> users = this.listUsers();
        return users.stream()
                .filter(u -> u.getProperty(this.userModel.getUsername()).getObject().toString().equals(username))
                .findFirst();
    }

    @Override
    public Optional<Resource> getUserByEmail(String email) {
        List<Resource> users = this.listUsers();
        return users.stream()
                .filter(u -> u.getProperty(this.userModel.getEmail()).getObject().toString().equals(email))
                .findFirst();
    }

    @Override
    public boolean existsByUsername(String username) {
        Optional<Resource> user = this.getUserByUsername(username);
        return user.isPresent();
    }

    @Override
    public boolean existsByEmail(String email) {
        Optional<Resource> user = this.getUserByEmail(email);
        return user.isPresent();
    }

    @Override
    public void deleteUser(String URI) {
        if (URI == null || URI.isEmpty()) {
            throw new IllegalArgumentException();
        }
        fusekiClient.executeDeleteQuery(URI);
    }
}
