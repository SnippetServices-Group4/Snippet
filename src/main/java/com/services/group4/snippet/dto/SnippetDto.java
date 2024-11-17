package com.services.group4.snippet.dto;

import jakarta.validation.constraints.NotNull;

public record SnippetDto(
    String name,
    @NotNull(message = "Content is required") String content,
    String version,
    String language,
    String extension) {}
