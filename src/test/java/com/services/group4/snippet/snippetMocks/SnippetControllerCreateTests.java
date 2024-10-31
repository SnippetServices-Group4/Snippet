package com.services.group4.snippet.snippetMocks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.snippet.DotenvConfig;
import com.services.group4.snippet.controller.SnippetController;
import com.services.group4.snippet.dto.SnippetRequest;
import com.services.group4.snippet.model.Snippet;
import com.services.group4.snippet.services.PermissionService;
import com.services.group4.snippet.services.SnippetService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
public class SnippetControllerCreateTests {

    @BeforeAll
    public static void setup() {
      DotenvConfig.loadEnv();
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PermissionService permissionService;

    private static final String MODULE_CHECKER_PATH =
      "com.services.group4.snippet.communication.utils.ModuleChecker";


    @Test
    @EnabledIf(MODULE_CHECKER_PATH + "#isPermissionModuleRunning")
    void testCreateSnippetForUser_Success() throws Exception {
        SnippetRequest request = new SnippetRequest("Test Snippet","This is a test snippet.");

        Mockito.when(permissionService.createOwnership(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        mockMvc.perform(MockMvcRequestBuilders.post("/snippets/createByUser/1")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Snippet created"));
    }


}