package com.services.group4.snippet.services;

import com.services.group4.snippet.dto.SnippetDto;
import com.services.group4.snippet.model.Snippet;
import com.services.group4.snippet.repositories.SnippetRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SnippetService {
  private final String container = "snippets";
  final SnippetRepository snippetRepository;
  final BlobStorageService blobStorageService;

  @Autowired
  public SnippetService(
      SnippetRepository snippetRepository, BlobStorageService blobStorageService) {
    this.snippetRepository = snippetRepository;
    this.blobStorageService = blobStorageService;
  }

  public void createSnippet(SnippetDto snippetDto) {
    Snippet snippet =
        new Snippet(
            snippetDto.getTitle(), snippetDto.getContent(),
            snippetDto.getVersion(), snippetDto.getLanguage());

    snippetRepository.save(snippet);

    blobStorageService.saveSnippet(container, snippet.getId(), snippet.getContent());
  }

  public Snippet createSnippet2(SnippetDto snippetDto) {
    Snippet snippet =
        new Snippet(
            snippetDto.getTitle(), snippetDto.getContent(),
            snippetDto.getVersion(), snippetDto.getLanguage());

    snippetRepository.save(snippet);

    blobStorageService.saveSnippet(container, snippet.getId(), snippet.getContent());

    return snippet;
  }

  public Optional<String> getSnippet(Long snippetId) {
    Optional<Snippet> snippetOptional = this.snippetRepository.findSnippetById(snippetId);
    if (snippetOptional.isPresent()) {
      Snippet snippet = snippetOptional.get();
      Long id = snippet.getId();
      return blobStorageService.getSnippet(container, id);
    }
    return Optional.empty();
  }

  public Optional<List<String>> getAllSnippet() {
    return blobStorageService.getAllSnippets(container);
  }

  public Optional<Snippet> updateSnippet(Long id, SnippetDto snippetRequest) {
    Optional<Snippet> snippetOptional = snippetRepository.findById(id);
    if (snippetOptional.isPresent()) {
      Snippet snippet = snippetOptional.get();
      snippet.setTitle(snippetRequest.getTitle());
      snippet.setContent(snippetRequest.getContent());
      snippet.setLanguage(snippetRequest.getLanguage());
      snippet.setVersion(snippetRequest.getVersion());

      // Actualizar en el blob storage
      blobStorageService.saveSnippet(container, snippet.getId(), snippet.getContent());

      return Optional.of(snippetRepository.save(snippet));
    }
    return Optional.empty();
  }

  public String deleteSnippet(Long id) {
    Optional<Snippet> snippetOptional = snippetRepository.findById(id);
    if (snippetOptional.isPresent()) {
      Snippet snippet = snippetOptional.get();
      Long idSnippet = snippet.getId();
      snippetRepository.delete(snippet);

      blobStorageService.deleteSnippet(container, idSnippet);
      return "Snippet deleted";
    }
    return "Snippet not found";
  }
}
