package com.services.group4.snippet.dto.testcase.response;

import com.services.group4.snippet.common.states.test.TestState;
import java.util.List;

public record TestCaseResponseStateDto(
    Long testId, String name, TestState state, List<String> inputs, List<String> outputs) {}
