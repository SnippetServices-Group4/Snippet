package com.services.group4.snippet.dto;

public class SnippetRequest {
  private String title;
  private String content;

  // Constructor, getters y setters
  public SnippetRequest() {}

  public SnippetRequest(String title, String content) {
    this.title = title;
    this.content = content;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
