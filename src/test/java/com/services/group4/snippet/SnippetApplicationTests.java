package com.services.group4.snippet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SnippetApplicationTests {
  @BeforeAll
  public static void setup() {
    DotenvConfig.loadEnv();
  }

  @Test
  void contextLoads() {}
}
