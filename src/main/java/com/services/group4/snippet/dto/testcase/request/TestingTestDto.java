package com.services.group4.snippet.dto.testcase.request;

import java.util.List;

public record TestingTestDto (List<String> inputs, List<String> outputs, String language, String version) {
    public TestingTestDto(List<String> inputs, List<String> outputs) {
        this(inputs, outputs, "", "");
    }
}
