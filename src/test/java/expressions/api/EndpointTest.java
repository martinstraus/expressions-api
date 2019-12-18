/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expressions.api;

import static org.hamcrest.CoreMatchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 *
 * @author martinstraus
 */
@ExtendWith(SpringExtension.class)
@WebAppConfiguration()
@ContextConfiguration(classes = {Application.class})
@TestInstance(Lifecycle.PER_CLASS)
public class EndpointTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mock;

    @BeforeEach
    public void beforeEach() {
        mock = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void evaluateExpressions() throws Exception {
        final String testExpression = "{\"expressions\": [\"a+b\"], \"parameters\": {\"a\":1, \"b\":2}}";
        mock.perform(
            post("/expressions/evaluate")
                .content(testExpression)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(jsonPath("$[0].result", is(3)))
            .andExpect(jsonPath("$[0].expression", is("a+b")));
    }

    @Test
    public void createAndEvaluateFunction() throws Exception {
        String functionId = new TestData().randomFunctionId();
        String definition = String.format("def %1$s(x) <- x+1; %1$s(a)", functionId);
        String definitionPayload = String.format("{\"definition\": \"%s\"}", definition);
        mock.perform(post("/functions/" + functionId)
            .content(definitionPayload)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andDo(print());
        String contextPayload = String.format("{\"a\": 2}");
        mock.perform(post("/functions/" + functionId + "/evaluations")
            .content(contextPayload)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
            .andDo(print())
            .andExpect(header().string("Location", String.format("/functions/%s/evaluations/%d", functionId, 1)))
            .andExpect(jsonPath("$.result", is(3)))
            .andExpect(jsonPath("$.expression", is(definition)));
    }

    @Test
    public void returns404IfFunctionDoesntExist() throws Exception {
        String functionId = new TestData().randomFunctionId();
        String contextPayload = String.format("{\"a\": 2}");
        mock.perform(post("/functions/" + functionId + "/evaluation")
            .content(contextPayload)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    public void createAndEvaluateSpecificFunctionVersion() throws Exception {
        String functionId = new TestData().randomFunctionId();
        mock.perform(post("/functions/" + functionId)
            .content(definitionPayload(String.format("def %1$s(x) <- x+1; %1$s(a)", functionId)))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andDo(print());
        String updatedDefinition = String.format("def %1$s(x) <- x+2; %1$s(a)", functionId);
        mock.perform(post("/functions/" + functionId + "/versions/2.0.0")
            .content(definitionPayload(updatedDefinition))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andDo(print());
        String contextPayload = String.format("{\"a\": 2}");
        mock.perform(post("/functions/" + functionId + "/versions/2.0.0/evaluations")
            .content(contextPayload)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
            .andDo(print())
            .andExpect(header().string("Location", String.format("/functions/%s/evaluations/%d", functionId, 1)))
            .andExpect(jsonPath("$.result", is(4)))
            .andExpect(jsonPath("$.expression", is(updatedDefinition)));
    }

    private String definitionPayload(String definition) {
        return String.format("{\"definition\": \"%s\"}", definition);
    }
}
