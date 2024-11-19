package com.services.group4.snippet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// TODO: fix this test
@SpringBootTest
@Disabled("Disabled for bd")
class SnippetApplicationTests {
  @BeforeAll
  public static void setup() {
    DotenvConfig.loadEnv();
  }

  @Test
  void contextLoads() {}
}
