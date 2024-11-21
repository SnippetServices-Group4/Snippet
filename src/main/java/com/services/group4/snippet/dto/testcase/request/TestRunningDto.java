package com.services.group4.snippet.dto.testcase.request;

import java.util.List;

public record TestRunningDto(List<String> inputs, List<String> outputs, String language, String version) {
}
