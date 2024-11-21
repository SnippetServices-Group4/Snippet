package com.services.group4.snippet.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.snippet.DotenvConfig;
import com.services.group4.snippet.common.FullResponse;
import com.services.group4.snippet.common.Language;
import com.services.group4.snippet.common.states.snippet.LintStatus;
import com.services.group4.snippet.dto.snippet.response.CompleteSnippetResponseDto;
import com.services.group4.snippet.dto.snippet.response.ResponseDto;
import com.services.group4.snippet.dto.snippet.response.SnippetDto;
import com.services.group4.snippet.dto.snippet.response.SnippetResponseDto;
import com.services.group4.snippet.services.SnippetService;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SnippetController.class)
class SnippetControllerTests {

  @BeforeAll
  public static void setupEnv() {
    DotenvConfig.loadEnv();
  }

  @MockBean private SnippetService snippetService;

  @Autowired private MockMvc mockMvc;

  @Test
  public void testGetAllSnippets() throws Exception {
    List<SnippetResponseDto> snippetList =
        List.of(
            new SnippetResponseDto(
                1L,
                "Test Snippet 1",
                "user1",
                new Language("java", "1.8", ".java"),
                LintStatus.NON_COMPLIANT),
            new SnippetResponseDto(
                2L,
                "Test Snippet 2",
                "user2",
                new Language("python", "3.8", ".py"),
                LintStatus.NON_COMPLIANT));

    when(snippetService.getAllSnippet(anyString()))
        .thenReturn(
            FullResponse.create(
                "All snippets that has permission on", "snippetList", snippetList, HttpStatus.OK));

    mockMvc
        .perform(get("/snippets/getAll").header("userId", "user1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("All snippets that has permission on"))
        .andExpect(jsonPath("$.data.snippetList[0].name").value("Test Snippet 1"))
        .andExpect(jsonPath("$.data.snippetList[1].name").value("Test Snippet 2"));
  }

  @Test
  public void testGetSnippet() throws Exception {
    CompleteSnippetResponseDto completeSnippetResponseDto =
        new CompleteSnippetResponseDto(
            1L, "Test Snippet", "user1", "Content", new Language("java", "1.8", ".java"));
    when(snippetService.getSnippet(anyLong(), anyString()))
        .thenReturn(
            FullResponse.create(
                "Snippet found successfully",
                "snippet",
                completeSnippetResponseDto,
                HttpStatus.OK));

    mockMvc
        .perform(get("/snippets/get/1").header("userId", "user1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Snippet found successfully"))
        .andExpect(jsonPath("$.data.snippet.name").value("Test Snippet"));
  }

  @Test
  public void testUpdateSnippet() throws Exception {
    CompleteSnippetResponseDto completeSnippetResponseDto =
        new CompleteSnippetResponseDto(
            1L, "Test Snippet", "user1", "Content", new Language("java", "1.8", ".java"));
    when(snippetService.updateSnippet(anyLong(), any(SnippetDto.class), anyString()))
        .thenReturn(
            FullResponse.create(
                "Snippet updated successfully",
                "snippet",
                completeSnippetResponseDto,
                HttpStatus.OK));

    SnippetDto snippetDto = new SnippetDto(null, "Content update", null, null, null);
    mockMvc
        .perform(
            put("/snippets/update/1")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(snippetDto))
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
        .perform(delete("/snippets/delete/1").header("userId", "user1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Snippet deleted"))
        .andExpect(jsonPath("$.data.snippetId").value(1L));
  }

  @Test
  public void testShareSnippet() throws Exception {
    when(snippetService.shareSnippet(anyLong(), anyString(), anyString()))
        .thenReturn(
            FullResponse.create("Snippet shared successfully", "snippetId", 1L, HttpStatus.OK));

    mockMvc
        .perform(post("/snippets/share/1/with/user2").header("userId", "user1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Snippet shared successfully"))
        .andExpect(jsonPath("$.data.snippetId").value(1L));
  }

  @Test
  public void testCreateSnippet() throws Exception {
    CompleteSnippetResponseDto completeSnippetResponseDto =
        new CompleteSnippetResponseDto(
            1L, "Test Snippet", "user1", "Content", new Language("java", "1.8", ".java"));
    when(snippetService.createSnippet(any(SnippetDto.class), anyString(), anyString()))
        .thenReturn(
            FullResponse.create(
                "Snippet created successfully",
                "snippet",
                completeSnippetResponseDto,
                HttpStatus.CREATED));

    SnippetDto snippetDto = new SnippetDto("Test Snippet", "Content", "1.8", "java", ".java");
    mockMvc
        .perform(
            post("/snippets/create")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(snippetDto))
                .header("userId", "user1")
                .header("username", "user1"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.message").value("Snippet created successfully"))
        .andExpect(jsonPath("$.data.snippet.name").value("Test Snippet"));
  }

  @Test
  public void testGetAllSnippetsCatch() throws Exception {
    when(snippetService.getAllSnippet(anyString()))
        .thenThrow(new RuntimeException("Error fetching snippets"));

    mockMvc
        .perform(get("/snippets/getAll").header("userId", "user1"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Error fetching snippets"));
  }

  @Test
  void testGetSnippetInfo_Success() throws Exception {
    SnippetResponseDto snippetResponse =
        new SnippetResponseDto(1L, "Sample Snippet", "owner123", new Language("JAVA", "1.8", ".java"), LintStatus.NON_COMPLIANT);

    when(snippetService.getSnippetInfo(1L)).thenReturn(FullResponse.create("Snippet retrieved successfully", "data", snippetResponse, HttpStatus.OK));

    mockMvc
        .perform(get("/snippets/getInfo/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.data.snippetId").value(1L))
        .andExpect(jsonPath("$.data.data.name").value("Sample Snippet"))
        .andExpect(jsonPath("$.data.data.owner").value("owner123"))
        .andExpect(jsonPath("$.message").value("Snippet retrieved successfully"));
  }

}
