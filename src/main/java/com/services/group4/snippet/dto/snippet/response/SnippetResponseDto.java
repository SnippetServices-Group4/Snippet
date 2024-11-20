package com.services.group4.snippet.dto.snippet.response;

import com.services.group4.snippet.common.Language;
import com.services.group4.snippet.model.Snippet;
import com.services.group4.snippet.common.states.snippet.LintStatus;

public record SnippetResponseDto(
    Long snippetId, String name, String owner, Language language, LintStatus status) {
    public SnippetResponseDto {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (owner == null || owner.isBlank()) {
            throw new IllegalArgumentException("Owner cannot be null or empty");
        }
    }
    public SnippetResponseDto(Snippet snippet) {
        this(snippet.getId(), snippet.getName(), snippet.getOwner(), snippet.getLanguage());
    }
}
