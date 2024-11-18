package com.services.group4.snippet.dto.testCase.response;

import com.services.group4.snippet.common.Language;
import com.services.group4.snippet.common.states.test.TestState;

import java.util.List;

public record TestCaseResponseDto(
    Long testId, String name, List<String> inputs, List<String> outputs) {}
