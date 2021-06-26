package mk.ukim.finki.wbs.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import mk.ukim.finki.wbs.mapper.ModelToJsonLDMapper;
import mk.ukim.finki.wbs.model.jsonld.Question;
import mk.ukim.finki.wbs.model.jsonld.QuestionUserReaction;
import mk.ukim.finki.wbs.model.jsonld.User;
import mk.ukim.finki.wbs.service.AnswerService;
import mk.ukim.finki.wbs.service.QuestionService;
import org.apache.jena.rdf.model.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/questions")
public class QuestionRestController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final ModelToJsonLDMapper mapper;

    public QuestionRestController(QuestionService questionService,
                                  AnswerService answerService,
                                  ModelToJsonLDMapper mapper) {
        this.questionService = questionService;
        this.answerService = answerService;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<String>> listQuestions() {

        List<String> questions = this.questionService.getQuestions().stream().map(q -> {
            try {
                return this.mapper.mapQuestion(q);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }).map(q -> q.get()).collect(Collectors.toList());

        return Optional.of(questions)
                .map(ob -> ResponseEntity.ok().body(ob))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/details")
    public ResponseEntity<String> getQuestion(@RequestBody Question question) {
        ResponseEntity<String> response = ResponseEntity.notFound().build();
        try {
            String id = question.getId();
            if (id.isEmpty()) {
                return ResponseEntity.ok("");
            }
            Resource resource = this.questionService.getQuestion(question.getId()).get().getSubject();
            Optional<String> foundQuestion = this.mapper.mapQuestion(resource);
            return foundQuestion
                    .map(ob -> ResponseEntity.ok().body(ob))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }
        catch (JsonProcessingException ex) {
            ex.getMessage();
        }

        return response;
    }


    @PostMapping("/add")
    public ResponseEntity<String> addQuestion(@RequestBody Question question) {

        String URI = null;
        ResponseEntity<String> response = ResponseEntity.notFound().build();
        try {
            Resource resource = this.questionService.addQuestion(URI,
                    question.getPrefix(),
                    question.getTitle(),
                    question.getDescription(),
                    question.getWriter().getId(),
                    question.getTags()).get();
            Optional<String> savedQuestion = this.mapper.mapQuestion(resource);
            return savedQuestion
                    .map(ob -> ResponseEntity.ok().body(ob))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }
        catch (JsonProcessingException ex) {
            ex.getMessage();
        }

        return response;
    }

    @PostMapping("/delete")
    public void deleteQuestion(@RequestBody Question question) {
        this.questionService.deleteQuestion(question.getId());
    }

    @PostMapping("/answers")
    public ResponseEntity<List<String>> listAnswersByQuestion(@RequestBody Question question) {

        List<String> answers = this.answerService.getAnswersByQuestion(question.getId()).stream().map(a -> {
            try {
                return this.mapper.mapAnswer(a);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }).map(a -> a.get()).collect(Collectors.toList());

        return Optional.of(answers)
                .map(ob -> ResponseEntity.ok().body(ob))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/like")
    public ResponseEntity<Boolean> likeQuestion(@RequestBody QuestionUserReaction qur) {
        return this.questionService.likeQuestion(qur.getQuestionId(), qur.getUserId())
                .map(q -> ResponseEntity.ok().body(q))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/dislike")
    public ResponseEntity<Boolean> dislikeQuestion(@RequestBody QuestionUserReaction qur) {
        return this.questionService.dislikeQuestion(qur.getQuestionId(), qur.getUserId())
                .map(q -> ResponseEntity.ok().body(q))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
