package com.services.group4.snippet.controller;

import com.services.group4.snippet.dto.request.TestCaseRequestDto;
import com.services.group4.snippet.dto.response.TestCaseResponseDto;
import com.services.group4.snippet.dto.response.TestCaseResponseStateDto;
import com.services.group4.snippet.dto.snippet.response.ResponseDto;
import com.services.group4.snippet.services.TestCaseService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
  public ResponseEntity<ResponseDto<TestCaseResponseDto>> createTestCase(
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

  @PutMapping("/update/{testCaseId}/for/{snippetId}")
  public ResponseEntity<ResponseDto<TestCaseResponseStateDto>> updateTestCase(
      @RequestBody @Valid TestCaseRequestDto testCaseRequestDto,
      @RequestHeader("userId") String userId,
      @PathVariable Long testCaseId,
      @PathVariable Long snippetId) {
    return testCaseService.updateTestCase(testCaseRequestDto, userId, testCaseId, snippetId);
  }

  // TODO: add endpoint to test a test case :)
}
