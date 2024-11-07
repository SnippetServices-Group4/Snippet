package com.services.group4.snippet.dto;

import com.services.group4.snippet.common.Language;
import lombok.Data;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Generated
@Data
public class SnippetResponseDto {
  // TODO: should also return url
  private Long id;

  private String name;

  private String content;

  private Language language;

  public SnippetResponseDto() {}

  public SnippetResponseDto(Long id, String name, String content, Language language) {
    this.id = id;
    this.name = name;
    this.content = content;
    this.language = language;
  }
}
