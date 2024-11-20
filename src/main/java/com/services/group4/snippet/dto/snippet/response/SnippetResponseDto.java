package com.services.group4.snippet.dto.snippet.response;

import com.services.group4.snippet.common.Language;
import com.services.group4.snippet.common.states.snippet.LintStatus;

public record SnippetResponseDto(
    Long snippetId, String name, String owner, Language language, LintStatus status) {}
