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

  // funcaaaaa postman
  @PostMapping("/create")
  public ResponseEntity<ResponseDto<SnippetResponseDto>> createSnippet(
      @RequestBody @Valid SnippetDto snippetDto, @RequestHeader("userId") Long userId, @RequestHeader("username") String username) {
      return snippetService.createSnippet(snippetDto, username, userId);
  }

  @GetMapping("/get/{id}")
  public ResponseEntity<ResponseDto<SnippetResponseDto>> getSnippet(@PathVariable Long id, @RequestHeader("userId") Long userId) {
    Optional<SnippetResponseDto> snippet = snippetService.getSnippet(id, userId);
    return snippet
        .map(s -> new ResponseEntity<>(new ResponseDto<>("Snippet found successfully",s), HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(new ResponseDto<>("Snippet not found",null),HttpStatus.NOT_FOUND));
  }

  @GetMapping("/getAll")
  public ResponseEntity<ResponseDto<List<AllSnippetResponseDto>>> getAllSnippet(@RequestHeader("userId") Long userId) {
    return snippetService.getAllSnippet(userId);
  }


  @PutMapping("/update/{id}")
  public ResponseEntity<ResponseDto<SnippetResponseDto>> updateSnippet(
      @PathVariable Long id, @RequestBody SnippetDto snippetDto, @RequestHeader("userId") Long userId) {
    try {
      Optional<SnippetResponseDto> snippet = snippetService.updateSnippet(id, snippetDto, userId);

      return snippet
          .map(snippetResponseDto -> new ResponseEntity<>(new ResponseDto<>("Snippet updated successfully",snippetResponseDto), HttpStatus.OK))
          .orElseGet(() -> new ResponseEntity<>(new ResponseDto<>("Snippet not found",null),HttpStatus.NOT_FOUND));

    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // TODO: make it right
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<ResponseDto<Long>> deleteSnippet(@PathVariable Long id) {
      return snippetService.deleteSnippet(id);
  }

  @PostMapping("/share/{snippetId}/with/{targetUserId}")
  public ResponseEntity<ResponseDto<Long>> shareSnippet(
      @RequestHeader("userId") Long userId, @PathVariable Long snippetId, @PathVariable Long targetUserId) {
    return snippetService.shareSnippet(snippetId, userId, targetUserId);
  }
}
