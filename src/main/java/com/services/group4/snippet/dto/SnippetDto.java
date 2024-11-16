package com.services.group4.snippet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SnippetDto(
    @NotBlank(message = "Name is required") String name,
    @NotNull(message = "Content is required") String content,
    @NotNull(message = "Version is required") String version,
    @NotNull(message = "Language is required") String language) {}
