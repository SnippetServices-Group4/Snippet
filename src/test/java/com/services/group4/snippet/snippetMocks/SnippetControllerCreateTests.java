package com.services.group4.snippet.snippetMocks;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.snippet.DotenvConfig;
import com.services.group4.snippet.dto.SnippetRequest;
import com.services.group4.snippet.services.PermissionService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class SnippetControllerCreateTests {

  @BeforeAll
  public static void setup() {
    DotenvConfig.loadEnv();
  }

  @Autowired private MockMvc mockMvc;

  @MockBean private PermissionService permissionService;

  @Test
  void testCreateSnippetForUser_Success() throws Exception {
    SnippetRequest request = new SnippetRequest("Test Snippet", "This is a test snippet.");

    Mockito.when(permissionService.createOwnership(Mockito.anyLong(), Mockito.anyLong()))
        .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

    ObjectMapper objectMapper = new ObjectMapper();
    mockMvc
        .perform(
            post("/snippets/createByUser/{userId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(content().string("Snippet created"));
  }
}
