package com.services.group4.snippet.dto.testcase.response;

import java.util.List;

public record TestCaseResponseDto(
    Long testId, String name, List<String> inputs, List<String> outputs) {}
