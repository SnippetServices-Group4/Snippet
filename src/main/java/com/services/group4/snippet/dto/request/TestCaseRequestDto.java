package com.services.group4.snippet.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record TestCaseRequestDto(
    List<String> inputs, List<String> outputs, @NotBlank String name) {}
