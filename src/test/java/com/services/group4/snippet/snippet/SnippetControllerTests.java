package com.services.group4.snippet.snippet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.snippet.DotenvConfig;
import com.services.group4.snippet.model.Snippet;
import com.services.group4.snippet.repositories.SnippetRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

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

  @Autowired
  private SnippetRepository snippetRepository;

  @BeforeEach
  public void setup() {
    snippetRepository.deleteAll();
    Snippet snippet = new Snippet("Test Title", 1L);
    snippetRepository.save(snippet);
  }

  @Test
  @Order(1)
  public void testGetAllSnippets() throws Exception {
    mockMvc.perform(get("/snippet")).andExpect(status().isOk());
  }

  @Test
  @Order(2)
  public void testGetSnippetById() throws Exception {
    Optional<Snippet> optionalSnippet = snippetRepository.findSnippetByName("Test Title");

    if (optionalSnippet.isEmpty()) {
      throw new Exception("Snippet not found");
    }

    Snippet snippet = optionalSnippet.get();
    Long snippetID = snippet.getId();

    mockMvc
        .perform(get("/snippet/{id}", snippetID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Test Title"))
        .andExpect(jsonPath("$.content").value("Test Content"));

    System.out.println("Snippet by id: " + snippetRepository.findById(snippetID).orElse(null));
  }

  @Test
  @Order(3)
  public void testCreateSnippet() throws Exception {
    Snippet snippet = new Snippet("New Title", 1L);

    mockMvc
        .perform(
            post("/snippet/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(snippet)))
        .andExpect(status().isCreated())
        .andExpect(content().string("Snippet created"));
  }

  @Test
  @Order(4)
  public void testUpdateSnippet() throws Exception {
    Optional<Snippet> optionalSnippet = snippetRepository.findSnippetByName("Test Title");

    if (optionalSnippet.isEmpty()) {
      throw new Exception("Snippet not found");
    }

    Snippet updatedSnippet = optionalSnippet.get();
    updatedSnippet.setName("Updated Title");
    mockMvc
        .perform(
            put("/snippet/update/{id}", updatedSnippet.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedSnippet)))
        .andExpect(status().isOk())
        .andExpect(content().string("Snippet updated"));
  }

  @Test
  @Order(5)
  public void testDeleteSnippet() throws Exception {
    Optional<Snippet> optionalSnippet = snippetRepository.findSnippetByName("Test Title");

    if (optionalSnippet.isEmpty()) {
      throw new Exception("Snippet not found");
    }

    Snippet snippet = optionalSnippet.get();
    Long snippetID = snippet.getId();

    mockMvc
        .perform(delete("/snippet/delete/{id}", snippetID))
        .andExpect(status().isOk())
        .andExpect(content().string("Snippet deleted"));
  }
}
