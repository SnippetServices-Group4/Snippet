package com.services.group4.snippet.dto.testCase.request;

import java.util.List;

public record TestCaseRequestDto(List<String> inputs, List<String> outputs, String name) {
}
