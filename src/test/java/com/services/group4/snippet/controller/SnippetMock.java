package com.services.group4.snippet.controller;

import com.services.group4.snippet.common.Language;
import com.services.group4.snippet.model.Snippet;
import java.util.List;

public class SnippetMock {
  public static Snippet createSnippet(int num) {
    Language language = new Language("java", "1.8", ".java");
    return new Snippet("Test Snippet " + num, "user", language);
  }

  public static List<Snippet> createSnippets() {
    return List.of(createSnippet(1), createSnippet(2), createSnippet(3));
  }
}
