package com.services.group4.snippet.dto.testCase.request;

import java.util.List;

public record TestRunningDto(String testId, List<String> inputs, List<String> outputs, String language, String version) {
    public TestRunningDto {
        if (testId == null) {
            throw new IllegalArgumentException("The testId is required");
        }
    }

    public TestRunningDto(String testId, List<String> inputs, List<String> outputs) {
        this(testId, inputs, outputs, "", "");
    }
}