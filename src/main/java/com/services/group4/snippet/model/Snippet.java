package com.services.group4.snippet.model;

import com.services.group4.snippet.common.Language;
import com.services.group4.snippet.common.states.snippet.SnippetState;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Data;
import lombok.Generated;

@Generated
@Entity
@Data
public class Snippet {

  @SequenceGenerator(name = "snippet", sequenceName = "snippet_sequence")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "snippet")
  @Id
  private Long id;

  @NotBlank private String name;

  // TODO: add url
  // @NotBlank private String url;

  @NotBlank private Long owner;

  @Embedded private Language language;

  @Embedded private SnippetState status = new SnippetState();

  @OneToMany(mappedBy = "snippet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<TestCase> testCases;

  public Snippet() {}

  public Snippet(String name, Long owner, Language languageVersion) {
    this.name = name;
    this.owner = owner;
    this.language = languageVersion;
  }
}
