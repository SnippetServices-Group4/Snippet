package com.services.group4.snippet.dto;

public class SnippetRequest {
  private Long userId;
  private String title;
  private String content;

  // Constructor, getters y setters
  public SnippetRequest() {}

  public SnippetRequest(Long userId, String title, String content) {
    this.userId = userId;
    this.title = title;
    this.content = content;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
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
