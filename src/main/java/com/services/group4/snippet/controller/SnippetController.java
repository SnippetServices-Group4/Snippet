package com.services.group4.snippet.controller;

import com.services.group4.snippet.dto.SnippetRequest;
import com.services.group4.snippet.model.Snippet;
import com.services.group4.snippet.repository.SnippetRepository;
import com.services.group4.snippet.services.PermissionService;
import com.services.group4.snippet.services.SnippetService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/snippets")
public class SnippetController {

  private final SnippetRepository snippetRepository;

  public SnippetController(SnippetRepository snippetRepository) {
    this.snippetRepository = snippetRepository;
  }

  @PostMapping("/create")
  public ResponseEntity<String> createSnippet(@RequestBody Snippet snippet) {
    try {
      snippetRepository.save(snippet);
      System.out.println(
          "from controller" + snippetRepository.findById(snippet.getSnippetID()).orElse(null));
      return new ResponseEntity<>("Snippet created", HttpStatus.CREATED);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ResponseEntity<>(
          "Something went wrong creating the Snippet", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Snippet> getSnippetById(@PathVariable Long id) {
    Optional<Snippet> spt = snippetRepository.findById(id);
    System.out.println("from controller" + spt);
    return spt.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping
  public ResponseEntity<List<Snippet>> getAllSnippets() {
    List<Snippet> spt = snippetRepository.findAll();
    System.out.println("from controller" + spt);
    return new ResponseEntity<>(spt, HttpStatus.OK);
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<String> updateSnippet(
      @PathVariable Long id, @RequestBody Snippet snippetDetails) {
    Optional<Snippet> snippet = snippetRepository.findById(id);
    if (snippet.isPresent()) {
      Snippet existingSnippet = snippet.get();
      existingSnippet.setTitle(snippetDetails.getTitle());
      existingSnippet.setContent(snippetDetails.getContent());
      snippetRepository.save(existingSnippet);
      System.out.println("from controller" + snippetRepository.findById(id).orElse(null));

      return new ResponseEntity<>("Snippet updated", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("Snippet not found", HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteSnippet(@PathVariable Long id) {
    try {
      snippetRepository.deleteById(id);
      System.out.println("from controller" + snippetRepository.findById(id).orElse(null));

      return new ResponseEntity<>("Snippet deleted", HttpStatus.OK);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ResponseEntity<>(
          "Something went wrong deleting the Snippet", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private PermissionService permissionService;

  @PostMapping("/create2")
  public ResponseEntity<?> createSnippet2(@RequestBody SnippetRequest request) {
    // 1. Crear el snippet (ejemplo simulado de creación)
    Snippet snippet = SnippetService.convertToEntity(request);

    // 2. Enviar la solicitud a Permission (P) para crear la relación de ownership
    ResponseEntity<?> permissionResponse =
        permissionService.createOwnership(request.getUserId(), snippet.getSnippetID());

    // 3. Verificar si Permission (P) respondió con éxito
    if (permissionResponse.getStatusCode().is2xxSuccessful()) {
      return ResponseEntity.ok(snippet);
    } else {
      // Si falló, devolver el error al ReverseProxy y no crear el snippet
      return ResponseEntity.status(permissionResponse.getStatusCode())
          .body(permissionResponse.getBody());
    }
  }
  @PostMapping("/create3")
  public ResponseEntity<String> createSnippet3(@RequestBody String title, @RequestBody String content) {
    try {
      Snippet snippet = new Snippet(title, content);
      snippetRepository.save(snippet);
      System.out.println(
          "from controller" + snippetRepository.findById(snippet.getSnippetID()).orElse(null));
      return new ResponseEntity<>("Snippet created", HttpStatus.CREATED);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ResponseEntity<>(
          "Something went wrong creating the Snippet", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
