package mk.ukim.finki.wbs.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import mk.ukim.finki.wbs.mapper.ModelToJsonLDMapper;
import mk.ukim.finki.wbs.model.dto.AnswerDto;
import mk.ukim.finki.wbs.model.jsonld.Answer;
import mk.ukim.finki.wbs.model.jsonld.Question;
import mk.ukim.finki.wbs.model.jsonld.QuestionUserReaction;
import mk.ukim.finki.wbs.service.AnswerService;
import org.apache.jena.rdf.model.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/answers")
public class AnswerRestController {

    private final AnswerService answerService;
    private final ModelToJsonLDMapper mapper;

    public AnswerRestController(AnswerService answerService,
                                ModelToJsonLDMapper mapper) {
        this.answerService = answerService;
        this.mapper = mapper;
    }

    @PostMapping("/details")
    public ResponseEntity<String> getAnswer(@RequestBody Answer answer) {
        ResponseEntity<String> response = ResponseEntity.notFound().build();
        try {
            Resource resource = this.answerService.getAnswer(answer.getId()).get().getSubject();
            Optional<String> foundAnswer = this.mapper.mapAnswer(resource);
            return foundAnswer
                    .map(ob -> ResponseEntity.ok().body(ob))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }
        catch (JsonProcessingException ex) {
            ex.getMessage();
        }

        return response;
    }


    @PostMapping("/add")
    public ResponseEntity<String> addAnswer(@RequestBody Answer answer) {

        ResponseEntity<String> response = ResponseEntity.notFound().build();
        try {
            Resource resource = this.answerService.addAnswer(answer.getPrefix(),
                    answer.getExplanation(),
                    answer.getWriter().getId(),
                    answer.getQuestion()).orElseThrow(IllegalArgumentException::new);
            Optional<String> savedAnswer = this.mapper.mapAnswer(resource);
            return savedAnswer
                    .map(ob -> ResponseEntity.ok().body(ob))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }
        catch (JsonProcessingException ex) {
            ex.getMessage();
        }

        return response;
    }

    @PostMapping("/delete")
    public void deleteAnswer(@RequestBody Answer answer) {
        this.answerService.deleteAnswer(answer.getId());
    }

    @PostMapping("/like")
    public ResponseEntity<Boolean> likeAnswer(@RequestBody QuestionUserReaction qur) {
        return this.answerService.likeAnswer(qur.getQuestionId(), qur.getUserId())
                .map(a -> ResponseEntity.ok().body(a))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/dislike")
    public ResponseEntity<Boolean> dislikeAnswer(@RequestBody QuestionUserReaction qur) {
        return this.answerService.dislikeAnswer(qur.getQuestionId(), qur.getUserId())
                .map(a -> ResponseEntity.ok().body(a))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
