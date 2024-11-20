package com.services.group4.snippet.dto.testCase.request;

import jakarta.validation.constraints.NotNull;
import lombok.Generated;

@Generated
public record ProcessingRequestDto(
    @NotNull(message = "The version is required") String version,
    @NotNull(message = "The language is required") String language,
    String content) {}
