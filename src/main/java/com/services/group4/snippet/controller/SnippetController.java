package com.services.group4.snippet.controller;

import com.services.group4.snippet.common.FullResponse;
import com.services.group4.snippet.dto.snippet.response.*;
import com.services.group4.snippet.dto.testcase.request.TestRunningDto;
import com.services.group4.snippet.services.SnippetService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

// TODO: add logs in each service
@Slf4j
@RestController
@RequestMapping("/snippets")
public class SnippetController {

  private final SnippetService snippetService;

  @Autowired
  public SnippetController(SnippetService snippetService) {
    this.snippetService = snippetService;
  }

  @PostMapping("/create")
  public ResponseEntity<ResponseDto<CompleteSnippetResponseDto>> createSnippet(
      @RequestBody @Valid SnippetDto snippetDto,
      @RequestHeader("userId") String userId,
      @RequestHeader("username") String username) {
    return snippetService.createSnippet(snippetDto, username, userId);
  }

  @GetMapping("/get/{id}")
  public ResponseEntity<ResponseDto<CompleteSnippetResponseDto>> getSnippet(
      @PathVariable Long id, @RequestHeader("userId") String userId) {
    return snippetService.getSnippet(id, userId);
  }

  @GetMapping("/getInfo/{snippetId}")
  public ResponseEntity<ResponseDto<SnippetResponseDto>> getSnippetInfo(
      @PathVariable Long snippetId) {
    return snippetService.getSnippetInfo(snippetId);
  }

  @GetMapping("/getAll")
  public ResponseEntity<ResponseDto<List<SnippetResponseDto>>> getAllSnippet(
      @RequestHeader("userId") String userId) {
    try {
      return snippetService.getAllSnippet(userId);
    } catch (Exception e) {
      return FullResponse.create(e.getMessage(), "Snippet", null, HttpStatus.NOT_FOUND);
    }
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<ResponseDto<CompleteSnippetResponseDto>> updateSnippet(
      @PathVariable Long id,
      @RequestBody SnippetDto snippetDto,
      @RequestHeader("userId") String userId) {
    return snippetService.updateSnippet(id, snippetDto, userId);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<ResponseDto<Long>> deleteSnippet(
      @PathVariable Long id, @RequestHeader("userId") String userId) {
    return snippetService.deleteSnippet(id, userId);
  }

  @PostMapping("/share/{snippetId}/with/{targetUserId}")
  public ResponseEntity<ResponseDto<Long>> shareSnippet(
      @RequestHeader("userId") String userId,
      @PathVariable Long snippetId,
      @PathVariable String targetUserId) {
    return snippetService.shareSnippet(snippetId, userId, targetUserId);
  }

  @PostMapping("/runTest/{snippetId}")
  public ResponseEntity<ResponseDto<TestResponseDto>> runTest(
      @RequestBody TestRunningDto requestBody,
      @RequestHeader("userId") String userId,
      @PathVariable Long snippetId) {
    return snippetService.runTest(requestBody, userId, snippetId);
  }
}
