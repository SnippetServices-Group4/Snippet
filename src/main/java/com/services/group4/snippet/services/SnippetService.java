package com.services.group4.snippet.services;

import com.services.group4.snippet.common.Language;
import com.services.group4.snippet.dto.AllSnippetResponseDto;
import com.services.group4.snippet.dto.SnippetDto;
import com.services.group4.snippet.dto.SnippetResponseDto;
import com.services.group4.snippet.model.Snippet;
import com.services.group4.snippet.repositories.SnippetRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SnippetService {
  private final String container = "snippets";
  final SnippetRepository snippetRepository;
  final BlobStorageService blobStorageService;
  final PermissionService permissionService;

  @Autowired
  public SnippetService(
      SnippetRepository snippetRepository,
      BlobStorageService blobStorageService,
      PermissionService permissionService) {
    this.snippetRepository = snippetRepository;
    this.blobStorageService = blobStorageService;
    this.permissionService = permissionService;
  }

  public SnippetResponseDto createSnippet(SnippetDto snippetDto, String username, Long userId) {
    Language language = new Language(snippetDto.getLanguage(), snippetDto.getVersion());
    Snippet snippet = new Snippet(snippetDto.getName(), username, language);

    snippetRepository.save(snippet);

    blobStorageService.saveSnippet(container, snippet.getId(), snippetDto.getContent());

    permissionService.grantOwnerPermission(snippet.getId(), userId);
    return new SnippetResponseDto(
        snippet.getId(), snippet.getName(), snippetDto.getContent(), snippet.getLanguage());
  }

  public Optional<SnippetResponseDto> getSnippet(Long snippetId, Long userId) {
    Optional<Snippet> snippetOptional = this.snippetRepository.findSnippetById(snippetId);

    if (snippetOptional.isEmpty()) {
      throw new NoSuchElementException("Snippet not found");
    }

    if (!permissionService.hasPermissionOnSnippet(userId, snippetId)) {
      throw new SecurityException("User does not have permission to view snippet");
    }
    Snippet snippet = snippetOptional.get();

    Optional<String> content = blobStorageService.getSnippet(container, snippetId);

    if (content.isEmpty()) {
      throw new NoSuchElementException("Snippet content not found");
    }

    return Optional.of(
        new SnippetResponseDto(
            snippet.getId(), snippet.getName(), content.get(), snippet.getLanguage()));
  }

  // este no va a tener el content del snippet solo la data de la tabla para la UI
  public List<Optional<AllSnippetResponseDto>> getAllSnippet(Long userId) {
  return snippetRepository.findAll().stream()
      .filter(snippet -> permissionService.hasPermissionOnSnippet(userId, snippet.getId()))
      .map(snippet -> Optional.of(
          new AllSnippetResponseDto(
              snippet.getId(), snippet.getName(), snippet.getLanguage())))
      .toList();
}

  public Optional<SnippetResponseDto> updateSnippet(Long id, SnippetDto snippetRequest, Long userId) {
    Optional<Snippet> snippetOptional = snippetRepository.findById(id);

    if (snippetOptional.isEmpty()) {
      throw new NoSuchElementException("Snippet not found");
    }

    if (!permissionService.hasOwnerPermission(userId, id)) {
      throw new SecurityException("User does not have permission to update snippet");
    }

    Snippet snippet = snippetOptional.get();
    snippet.setName(snippetRequest.getName());
    Language language = new Language(snippetRequest.getLanguage(), snippetRequest.getVersion());
    snippet.setLanguage(language);

    blobStorageService.saveSnippet(container, snippet.getId(), snippetRequest.getContent());

    snippetRepository.save(snippet);

    return Optional.of(
        new SnippetResponseDto(
            snippet.getId(),
            snippet.getName(),
            snippetRequest.getContent(),
            snippet.getLanguage()));
  }

  public void deleteSnippet(Long id) {
    Optional<Snippet> snippetOptional = snippetRepository.findById(id);

    if (snippetOptional.isEmpty()) {
      throw new NoSuchElementException("Snippet not found");
    }

    Snippet snippet = snippetOptional.get();
    blobStorageService.deleteSnippet(container, snippet.getId());
    snippetRepository.delete(snippet);
  }
}
