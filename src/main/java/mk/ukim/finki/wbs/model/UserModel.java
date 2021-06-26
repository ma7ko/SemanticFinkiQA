package mk.ukim.finki.wbs.model;

import com.ibm.icu.text.Transliterator;
import lombok.Data;
import mk.ukim.finki.wbs.client.FusekiClient;
import mk.ukim.finki.wbs.model.enumeration.PropertyType;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.impl.PropertyImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
public class UserModel {

    private Model model;

    private Map<String, String> prefixes;

    private FusekiClient fusekiClient;

    private Property username;
    private Property email;
    private Property code;
    private Property role;
    private Property prefix;
    private Property uri;

//    @Autowired
//    public void configureFusekiClient(@Value("${fuseki.endpoint}") String baseUrl, @Value("${fuseki.dataset}") String dataset) {
//        fusekiClient = new FusekiClient(baseUrl + dataset);
//        this.model = fusekiClient.getUserModel();
//        this.prefixes = fusekiClient.getPrefixes();
//        this.createProperties();
//    }

    public UserModel(FusekiClient fusekiClient) {
        this.fusekiClient = fusekiClient;
        this.model = fusekiClient.getUserModel();
        this.prefixes = fusekiClient.getPrefixes();
        this.createProperties();
    }

    private void createProperties() {
        this.username = new PropertyImpl(this.prefixes.get("dbp").concat(PropertyType.username.getType()));
        this.email = new PropertyImpl(this.prefixes.get("dbp").concat(PropertyType.email.getType()));
        this.code = new PropertyImpl(this.prefixes.get("dbp").concat(PropertyType.code.getType()));
        this.role = new PropertyImpl(this.prefixes.get("dbp").concat(PropertyType.role.getType()));
        this.prefix = new PropertyImpl(this.prefixes.get("dbp").concat(PropertyType.prefix.getType()));
        this.uri = new PropertyImpl(this.prefixes.get("dbp").concat(PropertyType.uri.getType()));
    }

    public String getURIFromUsername(String username) {
        return username.replaceAll("\\s+","-");
    }
}
