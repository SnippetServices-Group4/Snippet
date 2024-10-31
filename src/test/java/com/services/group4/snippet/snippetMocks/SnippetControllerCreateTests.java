package com.services.group4.snippet.snippetMocks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.snippet.DotenvConfig;
import com.services.group4.snippet.controller.SnippetController;
import com.services.group4.snippet.dto.SnippetRequest;
import com.services.group4.snippet.repository.SnippetRepository;
import com.services.group4.snippet.services.PermissionService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SnippetControllerCreateTests {

    @BeforeAll
    public static void setup() {
      DotenvConfig.loadEnv();
    }

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  private final PermissionService permissionService = Mockito.mock(PermissionService.class);

  private static final String MODULE_CHECKER_PATH =
    "com.services.group4.snippet.communication.utils.ModuleChecker";

  @Test
  @EnabledIf(MODULE_CHECKER_PATH + "#isPermissionModuleRunning")
  public void testCreateSnippet() throws Exception {
    SnippetRequest snippet = new SnippetRequest("Test Title", "This is a test snippet");

    Mockito.when(permissionService.createOwnership(Mockito.anyLong(), Mockito.anyLong()))
          .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

    mockMvc
        .perform(
            post("/snippets/created")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(snippet)))
        .andExpect(status().isCreated())
        .andExpect(content().string("Snippet created"));
  }
}