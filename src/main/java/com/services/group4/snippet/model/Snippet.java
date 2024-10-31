package com.services.group4.snippet.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

@Entity
@Table
public class Snippet {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long snippetID;

  @Column(nullable = false)
  private String title;

  @Column(nullable = true)
  private String content;

  @Column(nullable = false)
  private String version;

  @Column(nullable = false)
  private String language;

  // Constructors
  public Snippet() {}

  public Snippet(String title, String content, String version, String language) {
    this.title = title;
    this.content = content;
    this.version = version;
    this.language = language;
  }

  // Getters and Setters
  public Long getSnippetID() {
    return snippetID;
  }

  public void setSnippetID(Long id) {
    this.snippetID = id;
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

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }
}
