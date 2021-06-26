package mk.ukim.finki.wbs.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ioinformarics.oss.jackson.module.jsonld.JsonldModule;
import mk.ukim.finki.wbs.model.AnswerModel;
import mk.ukim.finki.wbs.model.QuestionModel;
import mk.ukim.finki.wbs.model.UserModel;
import mk.ukim.finki.wbs.model.jsonld.Answer;
import mk.ukim.finki.wbs.model.jsonld.Question;
import mk.ukim.finki.wbs.model.jsonld.User;
import mk.ukim.finki.wbs.repository.AnswerRepository;
import mk.ukim.finki.wbs.repository.QuestionRepository;
import mk.ukim.finki.wbs.repository.UserRepository;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ModelToJsonLDMapper {

    private QuestionModel questionModel;
    private AnswerModel answerModel;
    private UserModel userModel;
    private AnswerRepository answerRepository;
    private ObjectMapper objectMapper;

    public ModelToJsonLDMapper(QuestionModel questionModel,
                               AnswerModel answerModel,
                               UserModel userModel,
                               AnswerRepository answerRepository) {
        this.questionModel = questionModel;
        this.answerModel = answerModel;
        this.userModel = userModel;
        this.answerRepository = answerRepository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JsonldModule());
    }

    public Optional<String> mapQuestion(Resource resource) throws JsonProcessingException {

        Map<Property, List<RDFNode>> properties = this.getPropertiesMap(resource);
        String title = properties.get(questionModel.getTitle()).stream().map(i -> i.toString()).findFirst().orElseThrow(IllegalArgumentException::new);
        String description = properties.get(questionModel.getDescription()).stream().map(i -> i.toString()).findFirst().orElseThrow(IllegalArgumentException::new);

        RDFNode userResource = properties.get(questionModel.getWriter()).stream().findFirst().orElseThrow(IllegalArgumentException::new);
        User user = this.mapUserExp(userResource.asResource()).orElseThrow(IllegalArgumentException::new);

        String prefix = properties.get(questionModel.getPrefix()).stream().map(i -> i.toString()).findFirst().orElseThrow(IllegalArgumentException::new);
        String uri = properties.get(questionModel.getUri()).stream().map(i -> i.toString()).findFirst().orElseThrow(IllegalArgumentException::new);
        String date = properties.get(questionModel.getDate()).stream().map(i -> i.toString()).findFirst().orElseThrow(IllegalArgumentException::new);

        List<RDFNode> likedByList = properties.containsKey(questionModel.getLikedBy()) ? properties.get(questionModel.getLikedBy()) : new ArrayList<>();

        List<User> likedBy = likedByList.stream().map(u -> {
            try {
                return this.mapUserExp(u.asResource());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull)
                .map(Optional::get)
                .collect(Collectors.toList());

        List<RDFNode> dislikedByList = properties.containsKey(questionModel.getDislikedBy()) ? properties.get(questionModel.getDislikedBy()) : new ArrayList<>();

        List<User> dislikedBy = dislikedByList.stream().map(u -> {
            try {
                return this.mapUserExp(u.asResource());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull)
                .map(Optional::get)
                .collect(Collectors.toList());

        List<String> tagList = properties.containsKey(questionModel.getTag()) ? properties.get(questionModel.getTag()).stream().map(i -> i.toString()).collect(Collectors.toList()) : new ArrayList<>();

        List<Resource> answerList = this.answerRepository.getAnswersByQuestion(resource);

        List<Answer> answers = answerList.stream().map(a -> {
            try {
                return this.mapAnswerExp(a.asResource());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull)
                .map(Optional::get)
                .collect(Collectors.toList());

        Question question = new Question(resource.toString(),
                title,
                description,
                user,
                likedBy,
                dislikedBy,
                tagList,
                answers,
                prefix,
                uri,
                date);

        String questionJsonLd = objectMapper.writeValueAsString(question);

        return Optional.of(questionJsonLd);
    }

    public Optional<String> mapAnswer(Resource resource) throws JsonProcessingException {

        Map<Property, List<RDFNode>> properties = this.getPropertiesMap(resource);

        String explanation = properties.get(answerModel.getExplanation()).stream().map(i -> i.toString()).findFirst().orElseThrow(IllegalArgumentException::new);

        RDFNode userResource = properties.get(answerModel.getWriter()).stream().findFirst().orElseThrow(IllegalArgumentException::new);
        User user = this.mapUserExp(userResource.asResource()).orElseThrow(IllegalArgumentException::new);

        String date = properties.get(answerModel.getDate()).stream().map(i -> i.toString()).findFirst().orElseThrow(IllegalArgumentException::new);
        String questionURI = properties.get(answerModel.getReplyOf()).stream().map(i -> i.toString()).findFirst().orElseThrow(IllegalArgumentException::new);
        String prefix = properties.get(answerModel.getPrefix()).stream().map(i -> i.toString()).findFirst().orElseThrow(IllegalArgumentException::new);
        String uri = properties.get(answerModel.getUri()).stream().map(i -> i.toString()).findFirst().orElseThrow(IllegalArgumentException::new);


        List<RDFNode> likedByList = properties.containsKey(answerModel.getLikedBy()) ? properties.get(answerModel.getLikedBy()) : new ArrayList<>();

        List<User> likedBy = likedByList.stream().map(u -> {
            try {
                return this.mapUserExp(u.asResource());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull)
                .map(Optional::get)
                .collect(Collectors.toList());

        List<RDFNode> dislikedByList = properties.containsKey(answerModel.getDislikedBy()) ? properties.get(answerModel.getDislikedBy()) : new ArrayList<>();

        List<User> dislikedBy = dislikedByList.stream().map(u -> {
            try {
                return this.mapUserExp(u.asResource());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull)
                .map(Optional::get)
                .collect(Collectors.toList());

        Answer answer = new Answer(
                resource.toString(),
                "",
                explanation,
                user,
                date,
                questionURI,
                likedBy,
                dislikedBy,
                prefix,
                uri
        );

        String answerJsonLd = objectMapper.writeValueAsString(answer);

        return Optional.of(answerJsonLd);
    }

    public Optional<String> mapUser(Resource resource) throws JsonProcessingException {
        Map<Property, List<RDFNode>> properties = this.getPropertiesMap(resource);

        String username = properties.get(this.userModel.getUsername()).stream().map(i -> i.toString()).findFirst().orElseThrow(IllegalArgumentException::new);
        String email = properties.get(this.userModel.getEmail()).stream().map(i -> i.toString()).findFirst().orElseThrow(IllegalArgumentException::new);
        String code = properties.get(this.userModel.getCode()).stream().map(i -> i.toString()).findFirst().orElseThrow(IllegalArgumentException::new);
        List<String> roles = properties.get(this.userModel.getRole()).stream().map(i -> i.toString()).collect(Collectors.toList());
        String prefix = properties.get(this.userModel.getPrefix()).stream().map(i -> i.toString()).findFirst().orElseThrow(IllegalArgumentException::new);
        String uri = properties.get(this.userModel.getUri()).stream().map(i -> i.toString()).findFirst().orElseThrow(IllegalArgumentException::new);

        User user = new User(resource.toString(),
                username,
                email,
                code,
                roles,
                prefix,
                uri);

        String userJsonLd = objectMapper.writeValueAsString(user);

        return Optional.of(userJsonLd);
    }


    public Optional<User> mapUserExp(Resource resource) throws JsonProcessingException {
        Map<Property, List<RDFNode>> properties = this.getPropertiesMap(resource);

        String username = properties.get(this.userModel.getUsername()).stream().map(i -> i.toString()).findFirst().orElseThrow(IllegalArgumentException::new);
        String email = properties.get(this.userModel.getEmail()).stream().map(i -> i.toString()).findFirst().orElseThrow(IllegalArgumentException::new);
        String code = properties.get(this.userModel.getCode()).stream().map(i -> i.toString()).findFirst().orElseThrow(IllegalArgumentException::new);
        List<String> roles = properties.get(this.userModel.getRole()).stream().map(i -> i.toString()).collect(Collectors.toList());
        String prefix = properties.get(this.userModel.getPrefix()).stream().map(i -> i.toString()).findFirst().orElseThrow(IllegalArgumentException::new);
        String uri = properties.get(this.userModel.getUri()).stream().map(i -> i.toString()).findFirst().orElseThrow(IllegalArgumentException::new);

        User user = new User(resource.toString(),
                username,
                email,
                code,
                roles,
                prefix,
                uri);


        return Optional.of(user);
    }

    public Optional<Answer> mapAnswerExp(Resource resource) throws JsonProcessingException {

        Map<Property, List<RDFNode>> properties = this.getPropertiesMap(resource);

        String explanation = properties.get(answerModel.getExplanation()).stream().map(i -> i.toString()).findFirst().orElseThrow(IllegalArgumentException::new);

        RDFNode userResource = properties.get(answerModel.getWriter()).stream().findFirst().orElseThrow(IllegalArgumentException::new);
        User user = this.mapUserExp(userResource.asResource()).orElseThrow(IllegalArgumentException::new);

        String date = properties.get(answerModel.getDate()).stream().map(i -> i.toString()).findFirst().orElseThrow(IllegalArgumentException::new);
        String questionURI = properties.get(answerModel.getReplyOf()).stream().map(i -> i.toString()).findFirst().orElseThrow(IllegalArgumentException::new);
        String prefix = properties.get(answerModel.getPrefix()).stream().map(i -> i.toString()).findFirst().orElseThrow(IllegalArgumentException::new);
        String uri = properties.get(answerModel.getUri()).stream().map(i -> i.toString()).findFirst().orElseThrow(IllegalArgumentException::new);


        List<RDFNode> likedByList = properties.containsKey(answerModel.getLikedBy()) ? properties.get(answerModel.getLikedBy()) : new ArrayList<>();

        List<User> likedBy = likedByList.stream().map(u -> {
            try {
                return this.mapUserExp(u.asResource());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull)
                .map(Optional::get)
                .collect(Collectors.toList());

        List<RDFNode> dislikedByList = properties.containsKey(answerModel.getDislikedBy()) ? properties.get(answerModel.getDislikedBy()) : new ArrayList<>();

        List<User> dislikedBy = dislikedByList.stream().map(u -> {
            try {
                return this.mapUserExp(u.asResource());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull)
                .map(Optional::get)
                .collect(Collectors.toList());

        Answer answer = new Answer(
                resource.toString(),
                "",
                explanation,
                user,
                date,
                questionURI,
                likedBy,
                dislikedBy,
                prefix,
                uri
        );
        return Optional.of(answer);
    }

    public Map<Property, List<RDFNode>> getPropertiesMap(Resource resource) {
        return resource.listProperties()
                .toList()
                .stream()
                .collect(Collectors.groupingBy(Statement::getPredicate,
                        Collectors.mapping(Statement::getObject, Collectors.toList())));

    }
}
