package com.services.group4.snippet.dto.testcase.request;

import java.util.List;

public record TestingRequestDto(String testId, List<String> inputs, List<String> outputs, String version, String language) {
    public TestingRequestDto(String testId, List<String> inputs, List<String> outputs) {
        this(testId, inputs, outputs, "", "");
    }
}
