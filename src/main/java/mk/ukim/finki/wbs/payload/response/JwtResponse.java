package mk.ukim.finki.wbs.payload.response;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private String id;
    private String username;
    private String email;
    private String prefix;
    private String uri;
    private List<String> roles;

    public JwtResponse(String accessToken,
                       String id,
                       String username,
                       String email,
                       String prefix,
                       String uri,
                       List<String> roles) {
        this.accessToken = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.prefix = prefix;
        this.uri = uri;
        this.roles = roles;
    }

    public JwtResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
