package mk.ukim.finki.wbs.model.jsonld;

import ioinformarics.oss.jackson.module.jsonld.annotation.*;
import lombok.Data;

import java.util.List;

@JsonldResource
@JsonldNamespace(name = "s", uri = "http://dbpedia.org/property/")
@JsonldType("http://dbpedia.org/resource/Answer")
@Data
public class Answer {

    @JsonldId
    private String id;

    @JsonldProperty("s:title")
    private String title;

    @JsonldProperty("s:explanation")
    private String explanation;

    @JsonldProperty("s:writer")
    private User writer;

    @JsonldProperty("s:date")
    private String date;

    @JsonldProperty("https://www.w3.org/Submission/sioc-spec/#term_reply_of")
    private String question;

    @JsonldProperty("http://purl.org/net/soron/likedBy")
    private List<User> likedBy;

    @JsonldProperty("http://purl.org/net/soron/dislikedBy")
    private List<User> dislikedBy;

    @JsonldProperty("s:prefix")
    private String prefix = "http://localhost:3000/answers/details/";

    @JsonldProperty("s:identifier")
    private String uri;

    public Answer(String id,
                  String title,
                  String explanation,
                  User writer,
                  String date,
                  String question,
                  List<User> likedBy,
                  List<User> dislikedBy,
                  String prefix,
                  String uri) {

        this.id = id;
        this.title = title;
        this.explanation = explanation;
        this.writer = writer;
        this.date = date;
        this.question = question;
        this.likedBy = likedBy;
        this.dislikedBy = dislikedBy;
        this.prefix = prefix;
        this.uri = uri;
    }

}
