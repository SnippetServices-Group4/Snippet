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
  private Long id;

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
}
