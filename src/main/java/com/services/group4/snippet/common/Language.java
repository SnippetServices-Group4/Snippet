package com.services.group4.snippet.common;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Generated;

@Generated
@Embeddable
@Data
public class Language {

  @NotBlank private String langName;

  @NotBlank private String version;

  public Language() {}

  public Language(String language, String version) {
    this.langName = language.toLowerCase();
    this.version = version.toLowerCase();
  }
}
