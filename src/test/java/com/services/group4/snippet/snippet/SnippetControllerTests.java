package com.services.group4.snippet.snippet;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.snippet.DotenvConfig;
import com.services.group4.snippet.common.FullResponse;
import com.services.group4.snippet.common.Language;
import com.services.group4.snippet.dto.snippet.response.CompleteSnippetResponseDto;
import com.services.group4.snippet.dto.snippet.response.SnippetDto;
import com.services.group4.snippet.dto.snippet.response.SnippetResponseDto;
import com.services.group4.snippet.services.SnippetService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SnippetControllerTests {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private SnippetService snippetService;

  private SnippetDto snippetDto;
  private CompleteSnippetResponseDto completeSnippetResponseDto;

  @BeforeAll
  public static void setupEnv() {
    DotenvConfig.loadEnv();
  }

  @BeforeEach
  public void setup() {
    snippetDto = new SnippetDto("Test Snippet", "java", "1.8", ".java", "public class Test {}");
    completeSnippetResponseDto = new CompleteSnippetResponseDto(1L, "Test Snippet", "user", "public class Test {}", new Language("java", "1.8", ".java"));
  }

  @Test
  public void testCreateSnippet() throws Exception {
    when(snippetService.createSnippet(any(SnippetDto.class), anyString(), anyString()))
        .thenReturn(FullResponse.create("Snippet created successfully", "snippet", completeSnippetResponseDto, HttpStatus.CREATED));

    mockMvc
        .perform(post("/snippets/create")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(snippetDto))
            .header("userId", "user1")
            .header("username", "user"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.message").value("Snippet created successfully"))
        .andExpect(jsonPath("$.data.snippet.name").value("Test Snippet"));
  }

  @Test
  public void testGetSnippet() throws Exception {
    when(snippetService.getSnippet(anyLong(), anyString()))
        .thenReturn(FullResponse.create("Snippet found successfully", "snippet", completeSnippetResponseDto, HttpStatus.OK));

    mockMvc
        .perform(get("/snippets/get/1")
            .header("userId", "user1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Snippet found successfully"))
        .andExpect(jsonPath("$.data.snippet.name").value("Test Snippet"));
  }

  @Test
  public void testGetAllSnippets() throws Exception {
      List<SnippetResponseDto> snippetList = List.of(
          new SnippetResponseDto(1L, "Test Snippet 1", "user1", new Language("java", "1.8", ".java")),
          new SnippetResponseDto(2L, "Test Snippet 2", "user2", new Language("python", "3.8", ".py"))
      );

      when(snippetService.getAllSnippet(anyString()))
          .thenReturn(FullResponse.create("All snippets that has permission on", "snippetList", snippetList, HttpStatus.OK));

      mockMvc
          .perform(get("/snippets/getAll")
              .header("userId", "user1"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.message").value("All snippets that has permission on"))
          .andExpect(jsonPath("$.data.snippetList[0].name").value("Test Snippet 1"))
          .andExpect(jsonPath("$.data.snippetList[1].name").value("Test Snippet 2"));
  }

  @Test
  public void testUpdateSnippet() throws Exception {
    when(snippetService.updateSnippet(anyLong(), any(SnippetDto.class), anyString()))
        .thenReturn(FullResponse.create("Snippet updated successfully", "snippet", completeSnippetResponseDto, HttpStatus.OK));

    mockMvc
        .perform(put("/snippets/update/1")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(snippetDto))
            .header("userId", "user1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Snippet updated successfully"))
        .andExpect(jsonPath("$.data.snippet.name").value("Test Snippet"));
  }

  @Test
  public void testDeleteSnippet() throws Exception {
    when(snippetService.deleteSnippet(anyLong(), anyString()))
        .thenReturn(FullResponse.create("Snippet deleted", "snippetId", 1L, HttpStatus.OK));

    mockMvc
        .perform(delete("/snippets/delete/1")
            .header("userId", "user1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Snippet deleted"))
        .andExpect(jsonPath("$.data.snippetId").value(1L));
  }
}