package com.services.group4.snippet.dto;

import com.services.group4.snippet.common.Language;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

public record SnippetResponseDto(
    Long snippetId,
    String name,
    String owner,
    String content,
    Language language) {
}
