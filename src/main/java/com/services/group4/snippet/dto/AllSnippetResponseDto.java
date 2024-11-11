package com.services.group4.snippet.dto;

import com.services.group4.snippet.common.Language;
import lombok.Data;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

public record AllSnippetResponseDto(
    Long snippetId,
    String name,
    String owner,
    Language language) {
}
