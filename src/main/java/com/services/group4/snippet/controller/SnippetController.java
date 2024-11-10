package com.services.group4.snippet.controller;

import com.services.group4.snippet.dto.AllSnippetResponseDto;
import com.services.group4.snippet.dto.ResponseDto;
import com.services.group4.snippet.dto.SnippetDto;
import com.services.group4.snippet.dto.SnippetResponseDto;
import com.services.group4.snippet.services.PermissionService;
import com.services.group4.snippet.services.SnippetService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/snippets")
public class SnippetController {

  private final SnippetService snippetService;

  @Autowired
  public SnippetController(SnippetService snippetService, PermissionService permissionService) {
    this.snippetService = snippetService;
  }

  @PostMapping("/create")
  public ResponseEntity<ResponseDto<SnippetResponseDto>> createSnippet(
      @RequestBody @Valid SnippetDto snippetDto, @RequestHeader("userId") Long userId, @RequestHeader("username") String username) {
      return snippetService.createSnippet(snippetDto, username, userId);
  }

  @GetMapping("/get/{id}")
  public ResponseEntity<ResponseDto<SnippetResponseDto>> getSnippet(@PathVariable Long id, @RequestHeader("userId") Long userId) {
    return snippetService.getSnippet(id, userId);
  }

  @GetMapping("/getAll")
  public ResponseEntity<ResponseDto<List<AllSnippetResponseDto>>> getAllSnippet(@RequestHeader("userId") Long userId) {
    return snippetService.getAllSnippet(userId);
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<ResponseDto<SnippetResponseDto>> updateSnippet(
      @PathVariable Long id, @RequestBody SnippetDto snippetDto, @RequestHeader("userId") Long userId) {
    try {
      return snippetService.updateSnippet(id, snippetDto, userId);
    } catch (Exception e) {
      return new ResponseEntity<>(new ResponseDto<>("Something went wrong updating the snippet", null), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<ResponseDto<Long>> deleteSnippet(@PathVariable Long id, @RequestHeader("userId") Long userId) {
      return snippetService.deleteSnippet(id, userId);
  }

  @PostMapping("/share/{snippetId}/with/{targetUserId}")
  public ResponseEntity<ResponseDto<Long>> shareSnippet(
      @RequestHeader("userId") Long userId, @PathVariable Long snippetId, @PathVariable Long targetUserId) {
    return snippetService.shareSnippet(snippetId, userId, targetUserId);
  }
}
