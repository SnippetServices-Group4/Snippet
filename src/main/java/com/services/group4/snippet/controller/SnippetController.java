package com.services.group4.snippet.controller;

import com.services.group4.snippet.dto.SnippetDto;
import com.services.group4.snippet.model.Snippet;

import com.services.group4.snippet.services.SnippetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/snippet")
public class SnippetController {

  private final SnippetService snippetService;

  @Autowired
  public SnippetController(SnippetService snippetService) {
    this.snippetService = snippetService;
  }

  @PostMapping("/create")
  public ResponseEntity<Long> createSnippet(@RequestBody @Valid SnippetDto snippetDto) {
    Snippet snippet;
    try {
      snippet = snippetService.createSnippet(snippetDto);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<>(snippet.getId(), HttpStatus.CREATED);
  }

  @GetMapping()
  public ResponseEntity<String> getSnippet(@RequestParam Long snippetId) {
    Optional<String> snippet = snippetService.getSnippet(snippetId);
    if (snippet.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(snippet.get(), HttpStatus.OK);
  }
}
