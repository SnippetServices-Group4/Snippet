package com.services.group4.snippet.communication;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class ModulesCommunicationTest {
  @Autowired private MockMvc mockMvc;

  @Test
  void testOwnPermissionCommunication() throws Exception {
    this.mockMvc
        .perform(get("/test/permission/communication"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.source").value("Snippet"))
        .andExpect(jsonPath("$.message").value("Communication from Permission to Snippet works!"));
  }

  @Test
  void testOwnParserCommunication() throws Exception {
    this.mockMvc
        .perform(get("/test/parser/communication"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.source").value("Snippet"))
        .andExpect(jsonPath("$.message").value("Communication from Parser to Snippet works!"));
  }
}
