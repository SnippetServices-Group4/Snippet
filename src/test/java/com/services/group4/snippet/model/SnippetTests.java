package com.services.group4.snippet.model;

import com.services.group4.snippet.DotenvConfig;
import com.services.group4.snippet.common.Language;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SnippetTests {

  @BeforeAll
  public static void setupEnv() {
    DotenvConfig.loadEnv();
  }

  @Test
  public void testSnippetCreation() {
    Language language = new Language("java", "1.8", ".java");
    Snippet snippet = new Snippet("Test Snippet", "user", language);

    assertNotNull(snippet);
    assertEquals("Test Snippet", snippet.getName());
    assertEquals("user", snippet.getOwner());
    assertEquals(language, snippet.getLanguage());
  }
}
