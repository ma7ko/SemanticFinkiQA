package mk.ukim.finki.wbs.payload.request;

import lombok.Data;

@Data
public class LogInRequest {
    private String username;
    private String password;

    public LogInRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
