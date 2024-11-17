package com.services.group4.snippet.dto.snippet.response;

import com.services.group4.snippet.common.Language;

public record CompleteSnippetResponseDto(
    Long snippetId, String name, String owner, String content, Language language) {}
