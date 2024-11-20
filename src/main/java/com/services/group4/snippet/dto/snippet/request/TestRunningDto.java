package com.services.group4.snippet.dto.snippet.request;

import java.util.List;

public record TestRunningDto(Long snippetId, String testId, List<String> inputs, List<String> outputs, String language, String version) {
    public TestRunningDto {
        if (snippetId == null || testId == null) {
            throw new IllegalArgumentException("The testId and userId are required");
        }
    }

    public TestRunningDto(Long snippetId, String testId, List<String> inputs, List<String> outputs) {
        this(snippetId, testId, inputs, outputs, "", "");
    }
}