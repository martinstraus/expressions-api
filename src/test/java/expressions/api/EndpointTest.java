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
        mock.perform(
                post("/expressions/evaluate")
                        .content(payload())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[0].result", is(3)))
                .andExpect(jsonPath("$[0].expression", is("a+b")));
    }

    private String payload() {
        return "{\"expressions\": [\"a+b\"], \"parameters\": {\"a\":1, \"b\":2}}";
    }
}
