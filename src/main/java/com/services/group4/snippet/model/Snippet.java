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

  private Long owner;

  private String name;

  private String content;

  public Snippet() {}

  public Snippet(String name, String content, Long owner) {
    this.name = name;
    this.content = content;
    this.owner = owner;
  }


}
