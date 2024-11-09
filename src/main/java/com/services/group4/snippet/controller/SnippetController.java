package com.services.group4.snippet.controller;

import com.services.group4.snippet.dto.AllSnippetResponseDto;
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

  // TODO: improve error handling
  @PostMapping("/create")
  public ResponseEntity<SnippetResponseDto> createSnippet(
      @RequestBody @Valid SnippetDto snippetDto, @RequestHeader("userId") Long userId, @RequestHeader("username") String username) {
    try {
      SnippetResponseDto response = snippetService.createSnippet(snippetDto, username, userId);

      return new ResponseEntity<>(response, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/get/{id}")
  public ResponseEntity<SnippetResponseDto> getSnippet(@PathVariable Long id, @RequestHeader("userId") Long userId) {
    Optional<SnippetResponseDto> snippet = snippetService.getSnippet(id, userId);
    return snippet
        .map(s -> new ResponseEntity<>(s, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping("/getAll")
  public ResponseEntity<List<AllSnippetResponseDto>> getAllSnippet(@RequestHeader("userId") Long userId) {

    List<Optional<AllSnippetResponseDto>> snippet = snippetService.getAllSnippet(userId);

    List<AllSnippetResponseDto> response = snippet
        .stream()
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());

    if (response.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } else {
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
  }


  @PutMapping("/update/{id}")
  public ResponseEntity<SnippetResponseDto> updateSnippet(
      @PathVariable Long id, @RequestBody SnippetDto snippetDto, @RequestHeader("userId") Long userId) {
    try {
      // TODO: take it right from the token
      Optional<SnippetResponseDto> snippet = snippetService.updateSnippet(id, snippetDto, userId);

      return snippet
          .map(snippetResponseDto -> new ResponseEntity<>(snippetResponseDto, HttpStatus.OK))
          .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Void> deleteSnippet(@PathVariable Long id) {
    try {
      snippetService.deleteSnippet(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/share/{snippetId}/with/{targetUserId}")
  public ResponseEntity<String> shareSnippet(
      @RequestHeader("userId") Long userId, @PathVariable Long snippetId, @PathVariable Long targetUserId) {
    return snippetService.shareSnippet(snippetId, userId, targetUserId);
  }
}
