package com.services.group4.snippet.snippet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.snippet.DotenvConfig;
import com.services.group4.snippet.dto.SnippetRequest;
import com.services.group4.snippet.model.Snippet;
import com.services.group4.snippet.repository.SnippetRepository;
import java.util.Optional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SnippetControllerTests {
  @BeforeAll
  public static void setupEnv() {
    DotenvConfig.loadEnv();
  }

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private SnippetRepository snippetRepository;

  @BeforeEach
  public void setup() {
    snippetRepository.deleteAll();
    Snippet snippet = new Snippet("Test Title", "Test Content");
    snippetRepository.save(snippet);
  }

  @Test
  @Order(1)
  public void testGetAllSnippets() throws Exception {
    mockMvc.perform(get("/snippets")).andExpect(status().isOk());
  }

  @Test
  @Order(2)
  public void testGetSnippetById() throws Exception {
    Optional<Snippet> optionalSnippet = snippetRepository.findByTitle("Test Title");

    if (optionalSnippet.isEmpty()) {
      throw new Exception("Snippet not found");
    }

    Snippet snippet = optionalSnippet.get();
    Long snippetID = snippet.getSnippetID();

    mockMvc
        .perform(get("/snippets/{id}", snippetID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Test Title"))
        .andExpect(jsonPath("$.content").value("Test Content"));

    System.out.println("Snippet by id: " + snippetRepository.findById(snippetID).orElse(null));
  }

  @Test
  @Order(3)
  public void testCreateSnippet() throws Exception {
    SnippetRequest snippet = new SnippetRequest("New Title", "New Content");

    mockMvc
        .perform(
            post("/snippets/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(snippet)))
        .andExpect(status().isCreated())
        .andExpect(content().string("Snippet created"));
  }

  @Test
  @Order(4)
  public void testUpdateSnippet() throws Exception {
    Optional<Snippet> optionalSnippet = snippetRepository.findByTitle("Test Title");

    if (optionalSnippet.isEmpty()) {
      throw new Exception("Snippet not found");
    }

    Snippet updatedSnippet = optionalSnippet.get();
    updatedSnippet.setTitle("Updated Title");
    mockMvc
        .perform(
            put("/snippets/update/{id}", updatedSnippet.getSnippetID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedSnippet)))
        .andExpect(status().isOk())
        .andExpect(content().string("Snippet updated"));
  }

  @Test
  @Order(5)
  public void testDeleteSnippet() throws Exception {
    Optional<Snippet> optionalSnippet = snippetRepository.findByTitle("Test Title");

    if (optionalSnippet.isEmpty()) {
      throw new Exception("Snippet not found");
    }

    Snippet snippet = optionalSnippet.get();
    Long snippetID = snippet.getSnippetID();

    mockMvc
        .perform(delete("/snippets/delete/{id}", snippetID))
        .andExpect(status().isOk())
        .andExpect(content().string("Snippet deleted"));
  }
}
