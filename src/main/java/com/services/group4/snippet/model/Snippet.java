package com.services.group4.snippet.model;

import com.services.group4.snippet.common.Language;
import com.services.group4.snippet.common.states.snippet.LintStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Generated;

@Generated
@Entity
@Data
public class Snippet {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank private String name;

  // TODO: add url
  // @NotBlank private String url;

  @NotBlank private String owner;

  @Embedded private Language language;

  @NotNull private LintStatus status;

  public Snippet() {}

  public Snippet(String name, String owner, Language languageVersion) {
    this(name, owner, languageVersion, LintStatus.NON_COMPLIANT);
  }

  public Snippet(String name, String owner, Language languageVersion, LintStatus status) {
    this.name = name;
    this.owner = owner;
    this.language = languageVersion;
    this.status = status;
  }
}
