package mk.ukim.finki.wbs.model.jsonld;


import lombok.Data;

@Data
public class QuestionUserReaction {
    private String questionId;
    private String userId;

    public QuestionUserReaction(String questionId, String userId) {
        this.questionId = questionId;
        this.userId = userId;
    }
}
