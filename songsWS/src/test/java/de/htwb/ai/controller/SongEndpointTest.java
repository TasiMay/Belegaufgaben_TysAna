package de.htwb.ai.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@SpringBootTest
@TestPropertySource(locations = "classpath:/test.properties")
@AutoConfigureMockMvc
public class SongEndpointTest {
    public static final String payloadFull = "{\"id\":\"1\",\"title\":\"title\",\"artist\":\"artist\",\"label\":\"label\",\"released\":42069}";
    public static final String payloadFull2 = "{\"id\":\"3\",\"title\":\"yeetus deletus\",\"artist\":\"someartist\",\"label\":\"somelabel\",\"released\":42069}";
    public static final String payloadFullNegativeId = "{\"id\":\"-1\",\"title\":\"fetus deletus\",\"artist\":\"someartist\",\"label\":\"somelabel\",\"released\":42069}";
    public static final String payloadNoTitle = "{\"id\":\"1\",\"title\":,\"artist\":\"someartist\",\"label\":\"somelabel\",\"released\":42069}";
    public static final String payloadEmpty = "{}";
    public static final String allSongs = "{\"id\":1,\"title\":\"someTitle\",\"artist\":\"bsmith\",\"label\":\"secret\",\"released\":2000}{\"id\":2,\"title\":\"someOtherTitle\",\"artist\":\"mjack\",\"label\":\"noSecret\",\"released\":2020}";

    @Autowired
    MockMvc mockMvc;

    @Test
    void getSongIDNotFoundShouldReturn404() throws Exception {
        mockMvc.perform(get("/songs/3"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getSongWrongInputShouldReturn400() throws Exception {
        mockMvc.perform(get("/songs/string"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllSongsShouldReturn200() throws Exception {
        MvcResult result = mockMvc.perform(get("/songs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String resultString = result.getResponse().getContentAsString();
        Assertions.assertEquals(allSongs, resultString);
    }

    @Test
    void getAllSongsInvalidPathShouldReturn404() throws Exception {
        mockMvc.perform(get("/songs blub"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteSongShouldReturn204() throws Exception {
        mockMvc.perform(delete("/songs/2"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteSongIDNotFoundShouldReturn404() throws Exception {
        mockMvc.perform(delete("/songs/12"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteInvalidInputShouldReturn400() throws Exception {
        mockMvc.perform(delete("/songs/string"))
                .andExpect(status().isBadRequest());
    }


    @Test
    void putSongShouldReturn204() throws Exception {
        String b = "";
        mockMvc.perform(put("/songs/1").content(payloadFull).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }

    @Test
    void putSongEmptyShouldReturn400() throws Exception {
        mockMvc.perform(put("/songs/1").content(payloadEmpty).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void putSongInvalidIdShouldReturn404() throws Exception {
        mockMvc.perform(put("/songs/-1").content(payloadFullNegativeId).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void putSongNoMatchingIDsShouldReturn400() throws Exception {
        mockMvc.perform(put("/songs/2").content(payloadFull).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void putSongNoTitleShouldReturn400() throws Exception {
        mockMvc.perform(put("/songs/1").content(payloadNoTitle).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void putSongNonExistentIDShouldReturn404() throws Exception {
        mockMvc.perform(put("/songs/3").content(payloadFull2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void postSongShouldReturnCreated() throws Exception {
        mockMvc.perform(post("/songs").content(payloadFull).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void postSongNoTitleShouldReturn400() throws Exception {
        mockMvc.perform(post("/songs").content(payloadNoTitle).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postSongNoContentShouldReturn400() throws Exception {
        mockMvc.perform(post("/songs").content(payloadEmpty).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
