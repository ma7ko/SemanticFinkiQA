package mk.ukim.finki.wbs;

import mk.ukim.finki.wbs.model.dto.QuestionDto;
import mk.ukim.finki.wbs.service.QuestionService;
import org.apache.jena.rdf.model.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class SemanticQaApplicationTests {

    MockMvc mockMvc;

    @Autowired
    QuestionService questionService;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testListQuestions() throws Exception {
        MockHttpServletRequestBuilder questionsRequest = MockMvcRequestBuilders.get("/api/questions");
        this.mockMvc.perform(questionsRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testDeleteQuestion() throws Exception {
        Optional<String> question = Optional.of(this.questionService.addQuestion("uri","prefix","title", "description", "userURI", new ArrayList<>()).get().toString());

        String questionId = new String();
        if (question.isPresent()) {
            Integer treeSize = question.get().split("/").length;
            questionId = Arrays.stream(question.get().split("/")).skip(treeSize-1).findFirst().get();
        }
        MockHttpServletRequestBuilder questionsRequest = MockMvcRequestBuilders.delete(String.format("/api/questions/delete/%s", questionId));
        this.mockMvc.perform(questionsRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void contextLoads() {
    }

}
