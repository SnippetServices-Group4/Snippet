package com.services.group4.snippet.controller;

import com.services.group4.snippet.dto.SnippetDto;
import com.services.group4.snippet.model.Snippet;
import com.services.group4.snippet.services.PermissionService;
import com.services.group4.snippet.services.SnippetService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/snippets")
public class SnippetController {

  private final SnippetService snippetService;
  private final PermissionService permissionService;

  @Autowired
  public SnippetController(SnippetService snippetService, PermissionService permissionService) {
    this.permissionService = permissionService;
    this.snippetService = snippetService;
  }

  @PostMapping("/create")
  public ResponseEntity<String> createSnippet(@RequestBody @Valid SnippetDto snippetDto) {
    try {
      snippetService.createSnippet(snippetDto);
      return new ResponseEntity<>("Snippet created", HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(
          "Something went wrong creating the Snippet", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<String> getSnippet(@PathVariable Long id) {
    Optional<String> snippet = snippetService.getSnippet(id);
    return snippet
        .map(s -> new ResponseEntity<>(s, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping
  public ResponseEntity<List<String>> getAllSnippets() {
    Optional<List<String>> spts = snippetService.getAllSnippet();
    System.out.println("from controller" + spts);
    return spts.map(s -> new ResponseEntity<>(s, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<String> updateSnippet(
      @PathVariable Long id, @RequestBody SnippetDto SnippetDto) {
    Optional<Snippet> snippet = snippetService.updateSnippet(id, SnippetDto);
    if (snippet.isPresent()) {
      return new ResponseEntity<>("Snippet updated", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("Snippet not found", HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteSnippet(@PathVariable Long id) {
    String smg = snippetService.deleteSnippet(id);
    if (smg.equals("Snippet deleted")) {
      return new ResponseEntity<>("Snippet deleted", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("Snippet not found", HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/createByUser/{userId}")
  public ResponseEntity<?> createSnippetForUser(
      @RequestBody SnippetDto request, @PathVariable Long userId) {

    // 1. Crear el snippet (ejemplo simulado de creación)
    Snippet snippet = snippetService.createSnippet2(request);
    Long snippetId = snippet.getId();

    // 2. Enviar la solicitud a Permission (P) para crear la relación de ownership
    ResponseEntity<?> permissionResponse = permissionService.createOwnership(userId, snippetId);

    // 3. Verificar si Permission (P) respondió con éxito
    if (permissionResponse.getStatusCode() == HttpStatus.CREATED) {
      return new ResponseEntity<>("Snippet created", HttpStatus.CREATED);
    } else {
      // Si falló, devolver el error al ReverseProxy y no crear el snippet
      deleteSnippet(snippetId);
      return ResponseEntity.status(permissionResponse.getStatusCode())
          .body(permissionResponse.getBody());
    }
  }

  //  @PostMapping("/createByUser/{userId}")
  //  public ResponseEntity<?> createSnippetForUser(
  //      @RequestBody SnippetRequest request, @PathVariable Long userId) {
  //
  //    // 1. Crear el snippet (ejemplo simulado de creación)
  //    Snippet convertToSnippet = SnippetService.convertToEntity(request);
  //    snippetRepository.save(convertToSnippet);
  //    Long snippetID = convertToSnippet.getId();
  //
  //    // 2. Enviar la solicitud a Permission (P) para crear la relación de ownership
  //    ResponseEntity<?> permissionResponse = permissionService.createOwnership(userId, snippetID);
  //
  //    // 3. Verificar si Permission (P) respondió con éxito
  //    if (permissionResponse.getStatusCode() == HttpStatus.CREATED) {
  //      return new ResponseEntity<>("Snippet created", HttpStatus.CREATED);
  //    } else {
  //      // Si falló, devolver el error al ReverseProxy y no crear el snippet
  //      deleteSnippet(snippetID);
  //      return ResponseEntity.status(permissionResponse.getStatusCode())
  //          .body(permissionResponse.getBody());
  //    }
  //
  //    try {
  //      snippetService.createSnippet(snippetDto);
  //      return new ResponseEntity<>("Snippet created", HttpStatus.CREATED);
  //    } catch (Exception e) {
  //      return new ResponseEntity<>("Something went wrong creating the
  // Snippet",HttpStatus.INTERNAL_SERVER_ERROR);
  //    }
  //  }
}
