package com.services.group4.snippet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Generated;

@Generated
@Data
public class SnippetDto {

  @NotBlank(message = "Name is required")
  private String name;

  @NotNull(message = "Content is required")
  private String content;

  @NotNull(message = "Version is required")
  private String version;

  @NotNull(message = "Language is required")
  private String language;

  public SnippetDto() {}

  public SnippetDto(String name, String content, String version, String language) {
    this.name = name;
    this.content = content;
    this.version = version;
    this.language = language;
  }
}
