package com.services.group4.snippet.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;
import lombok.Generated;

@Generated
@Entity
@Data
public class Snippet {
  @SequenceGenerator(name = "snippet", sequenceName = "snippet_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "snippet")
  @Id
  private Long snippetID;

  private String title;

  private String content;

  private String version;

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
