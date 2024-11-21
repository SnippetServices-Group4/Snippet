package com.services.group4.snippet.dto.request;

import jakarta.annotation.Nullable;

import java.util.List;

public record TestingRequestDto(@Nullable String testId, List<String> inputs, List<String> outputs) {
}
