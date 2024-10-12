package com.services.group4.snippet.communication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.services.group4.snippet.DotenvConfig;
import com.services.group4.snippet.model.CommunicationMessage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@AutoConfigureMockMvc
public class OuterModulesCommunicationTest {
  @BeforeAll
  public static void setup() {
    DotenvConfig.loadEnv();
  }

  @Configuration
  static class TestConfig {
    @Bean
    public RestTemplate restTemplate() {
      return new RestTemplate();
    }
  }

  @Autowired private RestTemplate restTemplate;

  private static final String MODULE_CHECKER_PATH =
      "com.services.group4.snippet.communication.utils.ModuleChecker";

  @Test
  @EnabledIf(MODULE_CHECKER_PATH + "#isParserModuleRunning")
  void fromSnippetToParserCommunicationTest() {
    System.out.println("ATTENTION: PARSER MODULE MUST BE RUNNING FOR THIS TEST TO PASS!");
    String url = "http://localhost:8082/test/snippet/communication";
    CommunicationMessage response = restTemplate.getForObject(url, CommunicationMessage.class);
    assertNotNull(response);
    assertEquals("Parser", response.source());
    assertEquals("Communication from Snippet to Parser works!", response.message());
  }

  @Test
  @EnabledIf(MODULE_CHECKER_PATH + "#isParserModuleRunning")
  void fromSnippetToPermissionCommunicationTest() {
    System.out.println("ATTENTION: PERMISSION MODULE MUST BE RUNNING FOR THIS TEST TO PASS!");
    String url = "http://localhost:8081/test/snippet/communication";
    CommunicationMessage response = restTemplate.getForObject(url, CommunicationMessage.class);
    assertNotNull(response);
    assertEquals("Permission", response.source());
    assertEquals("Communication from Snippet to Permission works!", response.message());
  }
}
