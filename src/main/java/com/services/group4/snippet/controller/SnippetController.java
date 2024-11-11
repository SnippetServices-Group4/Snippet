package com.services.group4.snippet.controller;

import com.services.group4.snippet.common.FullResponse;
import com.services.group4.snippet.dto.AllSnippetResponseDto;
import com.services.group4.snippet.dto.ResponseDto;
import com.services.group4.snippet.dto.SnippetDto;
import com.services.group4.snippet.dto.SnippetResponseDto;
import com.services.group4.snippet.services.SnippetService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/snippets")
public class SnippetController {

  private final SnippetService snippetService;

  @Autowired
  public SnippetController(SnippetService snippetService) {
    this.snippetService = snippetService;
  }

  @PostMapping("/create")
  public ResponseEntity<ResponseDto<SnippetResponseDto>> createSnippet(
      @RequestBody @Valid SnippetDto snippetDto, @RequestHeader("userId") String userId, @RequestHeader("username") String username) {
      return snippetService.createSnippet(snippetDto, username, userId);
  }

  @GetMapping("/get/{id}")
  public ResponseEntity<ResponseDto<SnippetResponseDto>> getSnippet(@PathVariable Long id, @RequestHeader("userId") String userId) {
    return snippetService.getSnippet(id, userId);
  }

  @GetMapping("/getAll")
  public ResponseEntity<ResponseDto<List<AllSnippetResponseDto>>> getAllSnippet(@RequestHeader("userId") String userId) {
    try {
      return snippetService.getAllSnippet(userId);
    } catch (Exception e) { //"User doesn't have name to view any snippet"
      return FullResponse.create("fuck " + e.getMessage(), "Snippet", null, HttpStatus.NOT_FOUND);
    }
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<ResponseDto<SnippetResponseDto>> updateSnippet(
      @PathVariable Long id, @RequestBody SnippetDto snippetDto, @RequestHeader("userId") String userId) {
    return snippetService.updateSnippet(id, snippetDto, userId);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<ResponseDto<Long>> deleteSnippet(@PathVariable Long id, @RequestHeader("userId") String userId) {
    return snippetService.deleteSnippet(id, userId);
  }

  @PostMapping("/share/{snippetId}/with/{targetUserId}")
  public ResponseEntity<ResponseDto<Long>> shareSnippet(
      @RequestHeader("userId") String userId, @PathVariable Long snippetId, @PathVariable String targetUserId) {
    return snippetService.shareSnippet(snippetId, userId, targetUserId);
  }
}
