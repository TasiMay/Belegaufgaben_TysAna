package de.htwb.ai.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(locations = "classpath:/test.properties")
@AutoConfigureMockMvc
public class AuthEndpointTest {

    private String payloadIncorrect = "{\"userId\":\"mmuster\",\"password\":\"geheim\"}";
    private String payloadOkay = "{\"userId\":\"bsmith\",\"password\":\"secret\"}";
    private String payloadInvalid = "{\"ERROR\":\"mmuster\",\"password\":\"geheim\"}";

    @Autowired
    MockMvc mockMvc;


    @Test
    void postUserShouldReturnOkForExistingId() throws Exception {
        mockMvc.perform(post("/auth").content(payloadOkay).contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void postUserShouldReturn401ForIncorrectLogin() throws Exception {
        mockMvc.perform(post("/auth").content(payloadIncorrect).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void postUserShouldReturn400ForInvalidLogin() throws Exception {
        mockMvc.perform(post("/auth").content(payloadInvalid).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
