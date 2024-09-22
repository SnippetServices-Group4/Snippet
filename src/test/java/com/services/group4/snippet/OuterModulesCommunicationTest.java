package com.services.group4.snippet;

import com.services.group4.snippet.model.CommunicationMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
public class OuterModulesCommunicationTest {
  @Configuration
  static class TestConfig {
    @Bean
    public RestTemplate restTemplate() {
      return new RestTemplate();
    }
  }

  @Autowired
  private RestTemplate restTemplate;

  @Test
  @EnabledIf("com.services.group4.snippet.utils.ModuleChecker#isParserModuleRunning")
  void testParserSnippetCommunication() {
    System.out.println("ATTENTION: PARSER MODULE MUST BE RUNNING FOR THIS TEST TO PASS!");
    String url = "http://localhost:8082/test/snippet/communication";
    CommunicationMessage response = restTemplate.getForObject(url, CommunicationMessage.class);
    assertNotNull(response);
    assertEquals("Parser", response.source());
    assertEquals("Communication between Parser and Snippet works!", response.message());
  }
}
