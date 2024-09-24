package com.services.group4.snippet.model;

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

    // Constructors
    public Snippet() {
    }

    public Snippet(String title, String content) {
        this.title = title;
        this.content = content;
    }

  public Snippet(Long snippetID, String title, String content) {
    this.snippetID = snippetID;
      this.title = title;
    this.content = content;
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
}
