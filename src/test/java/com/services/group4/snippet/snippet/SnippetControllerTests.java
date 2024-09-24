package com.services.group4.snippet.snippet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.snippet.model.Snippet;
import com.services.group4.snippet.repository.SnippetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SnippetControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SnippetRepository snippetRepository;

    @BeforeEach
    public void setup() {
      Snippet snippet = new Snippet(1L, "Test Title", "Test Content");
      snippetRepository.save(snippet);
      System.out.println("Snippet saved: " + snippetRepository.findById(1L).orElse(null));
    }

    @Test
    public void testGetAllSnippets() throws Exception {
        mockMvc.perform(get("/snippets"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetSnippetById() throws Exception {
        mockMvc.perform(get("/snippets/{id}",1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.content").value("Test Content"));
    }

    @Test
    public void testCreateSnippet() throws Exception {
      Snippet snippet = new Snippet(2L, "New Title", "New Content");

      mockMvc.perform(post("/snippets/create")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(snippet)))
              .andExpect(status().isCreated())
              .andExpect(content().string("Snippet created"));
    }

    @Test
    public void testUpdateSnippet() throws Exception {
        Snippet updatedSnippet = new Snippet(1L,"New Title", "Updated Content");
        updatedSnippet.setTitle("Updated Title");
        mockMvc.perform(put("/snippets/update/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedSnippet)))
                .andExpect(status().isOk())
                .andExpect(content().string("Snippet updated"));
    }

    @Test
    public void testDeleteSnippet() throws Exception {
        mockMvc.perform(delete("/snippets/delete/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("Snippet deleted"));
    }
}