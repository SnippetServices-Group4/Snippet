package com.services.group4.snippet.controller;

import com.services.group4.snippet.dto.snippet.response.ResponseDto;
import com.services.group4.snippet.dto.testCase.request.TestCaseRequestDto;
import com.services.group4.snippet.dto.testCase.response.TestCaseResponseDto;
import com.services.group4.snippet.dto.testCase.response.TestCaseResponseStateDto;
import com.services.group4.snippet.services.TestCaseService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/testCase")
public class TestCaseController {

  private final TestCaseService testCaseService;

  @Autowired
  public TestCaseController(TestCaseService testCaseService) {
    this.testCaseService = testCaseService;
  }

  @PostMapping("/createFor/{snippetId}")
  public ResponseEntity<ResponseDto<TestCaseResponseDto>> createSnippet(
      @RequestBody @Valid TestCaseRequestDto testCaseRequestDto,
      @RequestHeader("userId") String userId,
      @PathVariable Long snippetId) {
    return testCaseService.createTestCase(testCaseRequestDto, userId, snippetId);
  }

  @GetMapping("/getAll/{snippetId}")
  public ResponseEntity<ResponseDto<List<TestCaseResponseStateDto>>> getTestCase(
      @PathVariable Long snippetId) {
    return testCaseService.getTestCase(snippetId);
  }

  @DeleteMapping("/delete/{testCaseId}/for/{snippetId}")
  public ResponseEntity<ResponseDto<String>> deleteTestCase(
      @RequestHeader("userId") String userId,
      @PathVariable Long testCaseId,
      @PathVariable Long snippetId) {
    return testCaseService.deleteTestCase(userId, testCaseId, snippetId);
  }

}
