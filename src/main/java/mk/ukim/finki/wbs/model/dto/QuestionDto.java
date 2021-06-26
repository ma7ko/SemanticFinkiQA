package mk.ukim.finki.wbs.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuestionDto {
    private String title;
    private String description;
    private List<String> likedBy;
    private List<String> dislikedBy;
    private String user;
    private List<String> tags;
    private List<AnswerDto> answers;

    public QuestionDto(String title,
                       String description,
                       List<String> likedBy,
                       List<String> dislikedBy,
                       String user,
                       List<String> tags,
                       List<AnswerDto> answers) {
        this.title = title;
        this.description = description;
        this.likedBy = likedBy;
        this.dislikedBy = dislikedBy;
        this.user = user;
        this.tags = tags;
        this.answers = answers;
    }
}
