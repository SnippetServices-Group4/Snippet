package com.services.group4.snippet.services;

import com.services.group4.snippet.common.Language;
import com.services.group4.snippet.dto.AllSnippetResponseDto;
import com.services.group4.snippet.dto.ResponseDto;
import com.services.group4.snippet.dto.SnippetDto;
import com.services.group4.snippet.dto.SnippetResponseDto;
import com.services.group4.snippet.model.Snippet;
import com.services.group4.snippet.repositories.SnippetRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

  public ResponseEntity<ResponseDto<SnippetResponseDto>> createSnippet(SnippetDto snippetDto, String username, String userId) {
    Language language = new Language(snippetDto.getLanguage(), snippetDto.getVersion());
    Snippet snippet = new Snippet(snippetDto.getName(), username, language);

    snippetRepository.save(snippet);

    // TODO: save snippet content from blob storage from infra bucket
    //blobStorageService.saveSnippet(container, snippet.getId(), snippetDto.getContent());

    ResponseEntity<ResponseDto<Long>> response = permissionService.grantOwnerPermission(snippet.getId(), userId);

    if (response.getStatusCode().equals(HttpStatus.CREATED)) {
      return new ResponseEntity<>(new ResponseDto<>( "Snippet created successfully",
          new SnippetResponseDto(snippet.getId(), snippet.getName(), snippetDto.getContent(), snippet.getLanguage())),
          HttpStatus.CREATED);
    }
    snippetRepository.delete(snippet);
    return new ResponseEntity<>(new ResponseDto<>( "Something went wrong creating the snippet", null),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public ResponseEntity<ResponseDto<SnippetResponseDto>> getSnippet(Long snippetId, String userId) {
    Optional<Snippet> snippetOptional = this.snippetRepository.findSnippetById(snippetId);

    if (snippetOptional.isEmpty()) {
      return new ResponseEntity<>(new ResponseDto<>("Snippet not found", null), HttpStatus.NOT_FOUND);
    }

    ResponseEntity<ResponseDto<Boolean>> hasPermission = permissionService.hasPermissionOnSnippet(userId, snippetId);

    if (Objects.requireNonNull(hasPermission.getBody()).data() != null && !hasPermission.getBody().data()) {
      return new ResponseEntity<>(new ResponseDto<>("User does not have permission to view snippet", null), HttpStatus.FORBIDDEN);
    }

    Snippet snippet = snippetOptional.get();

    // TODO: get snippet content from blob storage from infra bucket
    Optional<String> content = "contenttttt".describeConstable(); //blobStorageService.getSnippet(container, snippetId);

    if (content.isEmpty()) {
      return new ResponseEntity<>(new ResponseDto<>("Snippet content not found", null), HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(new ResponseDto<>("Snippet found successfully",
        new SnippetResponseDto(snippet.getId(), snippet.getName(), content.get(), snippet.getLanguage())),
        HttpStatus.OK);
  }

  // este no va a tener el content del snippet solo la data de la tabla para la UI
  public ResponseEntity<ResponseDto<List<AllSnippetResponseDto>>> getAllSnippet(String userId) {
    ResponseEntity<ResponseDto<List<Long>>> snippetIds = permissionService.getAllowedSnippets(userId);

    if (snippetIds.getStatusCode().isError() || snippetIds.getBody() == null) {
      throw new SecurityException("User does not have permission to view snippets, because it has no snippets");
    }

    List<AllSnippetResponseDto> snippets =  snippetIds.getBody().data().stream()
        .map(snippetId -> snippetRepository.findSnippetById(snippetId)
            .map(snippet -> new AllSnippetResponseDto(snippet.getId(), snippet.getName(), snippet.getLanguage())))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();

    return new ResponseEntity<>(new ResponseDto<>("All snippets that has permission", snippets), HttpStatus.OK);
}

  public ResponseEntity<ResponseDto<SnippetResponseDto>> updateSnippet(Long id, SnippetDto snippetRequest, String userId) {
    Optional<Snippet> snippetOptional = snippetRepository.findById(id);

    if (snippetOptional.isEmpty()) {
      throw new NoSuchElementException("Snippet not found");
    }

    ResponseEntity<ResponseDto<Boolean>> hasPermission = permissionService.hasPermissionOnSnippet(userId, id);
    if (Objects.requireNonNull(hasPermission.getBody()).data() != null && !hasPermission.getBody().data()) {
      return new ResponseEntity<>(new ResponseDto<>(hasPermission.getBody().message(), null), HttpStatus.FORBIDDEN);
    }

    Snippet snippet = snippetOptional.get();
    snippet.setName(snippetRequest.getName());
    Language language = new Language(snippetRequest.getLanguage(), snippetRequest.getVersion());
    snippet.setLanguage(language);

    // TODO: update snippet content from blob storage infra bucket
    //blobStorageService.saveSnippet(container, snippet.getId(), snippetRequest.getContent());

    snippetRepository.save(snippet);

    return new ResponseEntity<>(new ResponseDto<>("Snippet updated successfully",
        new SnippetResponseDto(snippet.getId(), snippet.getName(), snippetRequest.getContent(), snippet.getLanguage())),
        HttpStatus.OK);
  }

  public ResponseEntity<ResponseDto<Long>> deleteSnippet(Long id, String userId) {
    Optional<Snippet> snippetOptional = snippetRepository.findById(id);

    if (snippetOptional.isEmpty()) {
      return new ResponseEntity<>(new ResponseDto<>("Snippet not found", id), HttpStatus.NOT_FOUND);
    }

    Snippet snippet = snippetOptional.get();

    // TODO: delete snippet content from blob storage from infra bucket
    //blobStorageService.deleteSnippet(container, snippet.getId());

    ResponseEntity<ResponseDto<Long>> responseOwnership = permissionService.deletePermissions(id, userId);
    if (responseOwnership.getStatusCode().isError()) {
      return new ResponseEntity<>(new ResponseDto<>(Objects.requireNonNull(responseOwnership.getBody()).message(), id), responseOwnership.getStatusCode());
    }

    snippetRepository.delete(snippet);
    return new ResponseEntity<>(new ResponseDto<>("Snippet deleted", id), HttpStatus.OK);
  }

  public ResponseEntity<ResponseDto<Long>> shareSnippet(Long snippetId, String ownerId, String targetUserId) {
    return permissionService.shareSnippet(snippetId, ownerId, targetUserId);
  }
}
