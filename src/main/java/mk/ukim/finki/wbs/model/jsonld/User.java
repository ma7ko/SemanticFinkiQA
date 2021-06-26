package mk.ukim.finki.wbs.model.jsonld;

import ioinformarics.oss.jackson.module.jsonld.annotation.*;
import lombok.Data;

import java.util.List;

@JsonldResource
@JsonldNamespace(name = "s", uri = "http://dbpedia.org/property/")
@JsonldType("http://dbpedia.org/resource/User")
@Data
public class User {

    @JsonldId
    private String id;

    @JsonldProperty("s:username")
    private String username;

    @JsonldProperty("s:email")
    private String email;

    @JsonldProperty("s:code")
    private String code;

    @JsonldProperty("s:role")
    private List<String> roles;

    @JsonldProperty("s:prefix")
    private String prefix = "http://localhost:3000/profile/";

    @JsonldProperty("s:identifier")
    private String uri;

    public User(String id,
                String username,
                String email,
                String code,
                List<String> roles,
                String prefix,
                String uri) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.code = code;
        this.roles = roles;
        this.prefix = prefix;
        this.uri = uri;
    }
}
