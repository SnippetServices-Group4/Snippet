package com.services.group4.snippet.dto;

public class SnippetUpdateRequest {
  private String content;
  private String version;
  private String language;

  public SnippetUpdateRequest(String content, String version, String language) {
    this.content = content;
    this.version = version;
    this.language = language;
  }
}
