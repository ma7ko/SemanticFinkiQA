package mk.ukim.finki.wbs.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class AnswerDto {
    private String title;
    private String explanation;
    private String user;
    private List<String> likedBy;
    private List<String> dislikedBy;
    private String questionURI;

    public AnswerDto(String title, String explanation, String user, List<String> likedBy, List<String> dislikedBy, String questionURI) {
        this.title = title;
        this.explanation = explanation;
        this.user = user;
        this.likedBy = likedBy;
        this.dislikedBy = dislikedBy;
        this.questionURI = questionURI;
    }
}
