package mk.ukim.finki.wbs.model.jsonld;

import ioinformarics.oss.jackson.module.jsonld.annotation.*;
import lombok.Data;
import org.apache.jena.rdf.model.Resource;

import java.util.List;

@JsonldResource
@JsonldNamespace(name = "s", uri = "http://dbpedia.org/property/")
@JsonldType("http://dbpedia.org/resource/Question")
@Data
public class Question {

    @JsonldId
    private String id;

    @JsonldProperty("s:title")
    private String title;

    @JsonldProperty("s:description")
    private String description;

    @JsonldProperty("s:writer")
    private User writer;

    @JsonldProperty("http://purl.org/net/soron/likedBy")
    private List<User> likedBy;

    @JsonldProperty("http://purl.org/net/soron/dislikedBy")
    private List<User> dislikedBy;

    @JsonldProperty("s:tag")
    private List<String> tags;

    @JsonldProperty("https://www.w3.org/Submission/sioc-spec/#term_has_reply")
    private List<Answer> answer;

    @JsonldProperty("s:prefix")
    private String prefix = "http://localhost:3000/questions/details/";

    @JsonldProperty("s:identifier")
    private String uri;

    @JsonldProperty("s:date")
    private String date;

    public Question(String id,
                    String title,
                    String description,
                    User writer,
                    List<User> likedBy,
                    List<User> dislikedBy,
                    List<String> tags,
                    List<Answer> answer,
                    String prefix,
                    String uri) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.writer = writer;
        this.likedBy = likedBy;
        this.dislikedBy = dislikedBy;
        this.tags = tags;
        this.answer = answer;
        this.prefix = prefix;
        this.uri = uri;
    }

    public Question() {}

    public Question(String id,
                    String title,
                    String description,
                    User writer,
                    List<User> likedBy,
                    List<User> dislikedBy,
                    List<String> tags,
                    List<Answer> answer,
                    String prefix,
                    String uri,
                    String date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.writer = writer;
        this.likedBy = likedBy;
        this.dislikedBy = dislikedBy;
        this.tags = tags;
        this.answer = answer;
        this.prefix = prefix;
        this.uri = uri;
        this.date = date;
    }

// constructor, getters, setters
}
