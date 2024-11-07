package com.services.group4.snippet.controller;

import com.services.group4.snippet.dto.SnippetDto;
import com.services.group4.snippet.dto.SnippetResponseDto;
import com.services.group4.snippet.services.PermissionService;
import com.services.group4.snippet.services.SnippetService;
import jakarta.validation.Valid;
import java.util.Optional;
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
    this.permissionService = permissionService;
  }

  // TODO: improve error handling
  @PostMapping("/create/{userId}")
  public ResponseEntity<SnippetResponseDto> createSnippet(
      @RequestBody @Valid SnippetDto snippetDto, @PathVariable Long userId, @RequestHeader("Authorization") String token) {
    try {
      String userName = snippetService.getUsername(token);
      SnippetResponseDto response = snippetService.createSnippet(snippetDto, userName);

      return new ResponseEntity<>(response, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/get/{id}")
  public ResponseEntity<SnippetResponseDto> getSnippet(@PathVariable Long id) {
    Optional<SnippetResponseDto> snippet = snippetService.getSnippet(id);
    return snippet
        .map(s -> new ResponseEntity<>(s, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<SnippetResponseDto> updateSnippet(
      @PathVariable Long id, @RequestBody SnippetDto snippetDto) {
    try {
      Optional<SnippetResponseDto> snippet = snippetService.updateSnippet(id, snippetDto);

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
}
