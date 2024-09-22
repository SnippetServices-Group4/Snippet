package com.services.group4.snippet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ModulesCommunicationTest {
  @Autowired
  private MockMvc mockMvc;

  @Test
  void testOwnPermissionCommunication() throws Exception {
    this.mockMvc.perform(get("/test/permission/communication"))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"source\":\"Snippet\",\"message\":\"Communication between Snippet and Permission works!\"}"));
  }

  @Test
  void testOwnParserCommunication() throws Exception {
    this.mockMvc.perform(get("/test/parser/communication"))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"source\":\"Snippet\",\"message\":\"Communication between Snippet and Parser works!\"}"));
  }
}
