package mk.ukim.finki.wbs;

import mk.ukim.finki.wbs.client.FusekiClient;
import mk.ukim.finki.wbs.mapper.ModelToJsonLDMapper;
import mk.ukim.finki.wbs.model.QuestionModel;
import mk.ukim.finki.wbs.model.UserModel;
import mk.ukim.finki.wbs.model.dto.AnswerDto;
import mk.ukim.finki.wbs.model.dto.QuestionDto;
import mk.ukim.finki.wbs.repository.AnswerRepository;
import mk.ukim.finki.wbs.repository.impl.QuestionRepositoryImpl;
import mk.ukim.finki.wbs.repository.impl.UserRepositoryImpl;
import mk.ukim.finki.wbs.service.impl.QuestionServiceImpl;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.class)
public class QuestionTests {

    @Mock
    private FusekiClient fusekiClient;

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private QuestionModel questionModel;

    @Mock
    private UserModel userModel;

    @Mock
    private ModelToJsonLDMapper mapper;


    private QuestionRepositoryImpl questionRepository;
    private QuestionServiceImpl questionService;
    private UserRepositoryImpl userRepository;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
        List<AnswerDto> answers = new ArrayList<>();
        questionRepository = Mockito.spy(new QuestionRepositoryImpl(fusekiClient, answerRepository, questionModel, mapper));
        userRepository = Mockito.spy(new UserRepositoryImpl(fusekiClient, userModel));
        questionService = Mockito.spy(new QuestionServiceImpl(questionRepository, userRepository, questionModel));
    }

    @Test
    public void testListQuestions() {
        List<Resource> questions = this.questionRepository.listQuestions();

        Mockito.verify(this.questionRepository).listQuestions();

        Assert.assertNotNull("Question list is null", questions);
        Assert.assertFalse("Question list is empty", questions.size() == 0);

    }

    @Test
    public void testGetQuestion() {
        Resource question = this.questionRepository.getQuestion("VebProgramiranje").get().getSubject();

        Mockito.verify(this.questionRepository).getQuestion("VebProgramiranje");

        Assert.assertNotNull("Question is null", question);
        Assert.assertFalse("Question does not have any properties", question.listProperties().toList().size() == 0);
    }

    @Test
    public void testSaveQuestion() {
        Optional<Resource> question = this.questionService.addQuestion("uri","prefix","title","description", "userURI", new ArrayList<>());

        Mockito.verify(this.questionService).addQuestion("uri","prefix","title","description", "userURI", new ArrayList<>());

        Map<Property, List<RDFNode>> map1 = new HashMap<>();

        if (question.isPresent()) {
            map1 = question.get().listProperties().toList().stream().collect(Collectors.groupingBy(Statement::getPredicate, Collectors.mapping(Statement::getObject, Collectors.toList())));
        }

        Assert.assertNotNull("Question is null", question.get());

        map1.forEach((key, values) -> {
            Assert.assertNotNull(String.format("Property %s is null", key), question.get().getProperty(key));
        });

    }

    @Test
    public void testDeleteQuestion() {

        this.questionRepository.deleteQuestion("title");

        Mockito.verify(this.questionRepository).deleteQuestion("title");

    }

    @Test
    public void testNullTitle() {
        Assert.assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> this.questionService.addQuestion("uri","prefix",null,
                        "description",
                        "userURI",
                        new ArrayList<>()));

        Mockito.verify(this.questionService).addQuestion("uri","prefix",null,
                "description",
                "userURI",
                new ArrayList<>());

    }

    @Test
    public void testNullDescription() {
        Assert.assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> this.questionService.addQuestion("uri","prefix","title",
                        null,
                        "userURI",
                        new ArrayList<>()));

        Mockito.verify(this.questionService).addQuestion("uri","prefix","title",
                null,
                "userURI",
                new ArrayList<>());

    }

    @Test
    public void testNullUserURI() {
        Assert.assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> this.questionService.addQuestion("uri","prefix","title",
                        "description",
                        null,
                        new ArrayList<>()));

        Mockito.verify(this.questionService).addQuestion("uri","prefix","title",
                "description",
                null,
                new ArrayList<>());

    }

    @Test
    public void testNullTagList() {
        Assert.assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> this.questionService.addQuestion("uri","prefix","title",
                        "description",
                        "userURI",
                        null));

        Mockito.verify(this.questionService).addQuestion("uri","prefix","title",
                "description",
                "userURI",
                null);

    }

    @Test
    public void testEmptyTitle() {
        Assert.assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> this.questionService.addQuestion("uri","prefix","",
                        "description",
                        "userURI",
                        new ArrayList<>()));

        Mockito.verify(this.questionService.addQuestion("uri","prefix","",
                "description",
                "userURI",
                new ArrayList<>()));

    }

    @Test
    public void testEmptyDescription() {
        Assert.assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> this.questionService.addQuestion("uri","prefix","title",
                        "",
                        "userURI",
                        new ArrayList<>()));

        Mockito.verify(this.questionService.addQuestion("uri","prefix","title",
                "",
                "userURI",
                new ArrayList<>()));

    }

    @Test
    public void testEmptyUserURI() {
        Assert.assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> this.questionService.addQuestion("uri","prefix","title",
                        "description",
                        "",
                        new ArrayList<>()));

        Mockito.verify(this.questionService.addQuestion("uri","prefix","title",
                "description",
                "",
                new ArrayList<>()));

    }

    @Test
    public void testNullDeleteQuestionURI() {
        Assert.assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> this.questionRepository.deleteQuestion((String)null));

        Mockito.verify(this.questionRepository).deleteQuestion((String)null);
    }

    @Test
    public void testEmptyDeleteQuestionURI() {
        Assert.assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> this.questionRepository.deleteQuestion(""));

        Mockito.verify(this.questionRepository).deleteQuestion("");
    }

    @Test
    public void testNullGetQuestionURI() {
        Assert.assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> this.questionRepository.getQuestion(null));

        Mockito.verify(this.questionRepository).getQuestion(null);
    }

    @Test
    public void testEmptyGetQuestionURI() {
        Assert.assertThrows("IllegalArgumentException expected",
                IllegalArgumentException.class,
                () -> this.questionRepository.getQuestion(""));

        Mockito.verify(this.questionRepository).getQuestion("");
    }


}
