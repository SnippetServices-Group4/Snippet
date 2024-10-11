package com.services.group4.snippet.snippet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.snippet.model.Snippet;
import com.services.group4.snippet.repository.SnippetRepository;
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

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  private static SnippetRepository snippetRepository;

  @BeforeAll
  public static void setup(@Autowired SnippetRepository snippetRepository) {
    SnippetControllerTests.snippetRepository = snippetRepository;
    snippetRepository.deleteAll();
    Snippet snippet = new Snippet("Test Title", "Test Content");
    snippetRepository.save(snippet);
  }

  @Test
  @Order(1)
  public void testGetAllSnippets() throws Exception {
    System.out.println(
        "Snippet saved: " + snippetRepository.findByTitle("Test Title").get().toJson());

    mockMvc.perform(get("/snippets")).andExpect(status().isOk());
  }

  @Test
  @Order(2)
  public void testGetSnippetById() throws Exception {
    System.out.println(
        "Snippet saved: " + snippetRepository.findByTitle("Test Title").get().toJson());

    mockMvc
        .perform(get("/snippets/{id}", 1L))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Test Title"))
        .andExpect(jsonPath("$.content").value("Test Content"));

    System.out.println("Snippet by id: " + snippetRepository.findById(1L).orElse(null));
  }

  @Test
  @Order(3)
  public void testCreateSnippet() throws Exception {
    System.out.println(
        "Snippet saved: " + snippetRepository.findByTitle("Test Title").get().toJson());

    Snippet snippet = new Snippet("New Title", "New Content");

    mockMvc
        .perform(
            post("/snippets/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(snippet)))
        .andExpect(status().isCreated())
        .andExpect(content().string("Snippet created"));

    System.out.println(
        "new Snippet saved: " + snippetRepository.findByTitle("New Title").get().toJson());
  }

  @Test
  @Order(4)
  public void testUpdateSnippet() throws Exception {
    System.out.println(
        "Snippet saved: " + snippetRepository.findByTitle("Test Title").get().toJson());

    Snippet updatedSnippet = snippetRepository.findById(1L).get();
    updatedSnippet.setTitle("Updated Title");
    mockMvc
        .perform(
            put("/snippets/update/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedSnippet)))
        .andExpect(status().isOk())
        .andExpect(content().string("Snippet updated"));

    System.out.println(
        "Snippet saved: " + snippetRepository.findByTitle("Updated Title").get().toJson());
  }

  @Test
  @Order(5)
  public void testDeleteSnippet() throws Exception {
    System.out.println(
        "Snippet saved: " + snippetRepository.findByTitle("Updated Title").get().toJson());

    mockMvc
        .perform(delete("/snippets/delete/{id}", 1L))
        .andExpect(status().isOk())
        .andExpect(content().string("Snippet deleted"));

    System.out.println("Snippet deleted: " + snippetRepository.findById(1L).orElse(null));
  }
}
