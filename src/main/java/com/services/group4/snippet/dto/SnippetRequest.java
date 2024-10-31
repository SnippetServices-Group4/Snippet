package com.services.group4.snippet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SnippetRequest {
  @JsonProperty("title")
  private String title;

  @JsonProperty("content")
  private String content;

  @JsonProperty("language")
  private String language;

  @JsonProperty("version")
  private String version;

  // Constructor, getters y setters
  public SnippetRequest() {}

  public SnippetRequest(String title, String content, String version, String language) {
    this.title = title;
    this.content = content;
    this.language = language;
    this.version = version;
  }

  public String getTitle() {
    return title;
  }

  public String getContent() {
    return content;
  }

  public String getLanguage() {
    return language;
  }

  public String getVersion() {
    return version;
  }
}
