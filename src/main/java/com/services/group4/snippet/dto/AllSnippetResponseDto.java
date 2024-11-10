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
public class AllSnippetResponseDto {
  // TODO: should also return url
  private Long id;

  private String name;

  private Language language;

  public AllSnippetResponseDto() {}

  public AllSnippetResponseDto(Long id, String name, Language language) {
    this.id = id;
    this.name = name;
    this.language = language;
  }
}
