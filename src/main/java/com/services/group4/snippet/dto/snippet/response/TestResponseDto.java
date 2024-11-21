package com.services.group4.snippet.dto.snippet.response;

import com.services.group4.snippet.common.states.test.TestState;

public record TestResponseDto(Long snippetId, String testId, TestState testState) {}
