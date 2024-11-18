package com.services.group4.snippet.services;

import com.services.group4.snippet.common.FullResponse;
import com.services.group4.snippet.common.Language;
import com.services.group4.snippet.dto.snippet.response.CompleteSnippetResponseDto;
import com.services.group4.snippet.dto.snippet.response.ResponseDto;
import com.services.group4.snippet.dto.snippet.response.SnippetDto;
import com.services.group4.snippet.dto.snippet.response.snippetResponseDto;
import com.services.group4.snippet.model.Snippet;
import com.services.group4.snippet.repositories.SnippetRepository;
import feign.FeignException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SnippetService {
  private final String container = "snippets";
  final SnippetRepository snippetRepository;
  final BlobStorageService blobStorageService;
  final PermissionService permissionService;
  final TestCaseService testCaseService;

  @Autowired
  public SnippetService(
      SnippetRepository snippetRepository,
      BlobStorageService blobStorageService,
      PermissionService permissionService,
      TestCaseService testCaseService) {
    this.snippetRepository = snippetRepository;
    this.blobStorageService = blobStorageService;
    this.permissionService = permissionService;
    this.testCaseService = testCaseService;
  }

  @Transactional
  public ResponseEntity<ResponseDto<CompleteSnippetResponseDto>> createSnippet(
      SnippetDto snippetDto, String username, String userId) {
    Language language =
        new Language(snippetDto.language(), snippetDto.version(), snippetDto.extension());
    Snippet snippet = new Snippet(snippetDto.name(), username, language);

    snippetRepository.save(snippet);

    blobStorageService.saveSnippet(container, snippet.getId(), snippetDto.content());

    ResponseEntity<ResponseDto<Long>> response =
        permissionService.grantOwnerPermission(snippet.getId(), userId);

    if (response.getStatusCode().equals(HttpStatus.CREATED)) {
      CompleteSnippetResponseDto completeSnippetResponseDto =
          new CompleteSnippetResponseDto(
              snippet.getId(),
              snippet.getName(),
              snippet.getOwner(),
              snippetDto.content(),
              snippet.getLanguage());
      return FullResponse.create(
          "Snippet created successfully",
          "snippet",
          completeSnippetResponseDto,
          HttpStatus.CREATED);
    }
    return FullResponse.create(
        "Something went wrong creating the snippet",
        "snippet",
        null,
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public ResponseEntity<ResponseDto<CompleteSnippetResponseDto>> getSnippet(
      Long snippetId, String userId) {
    Optional<Snippet> snippetOptional = this.snippetRepository.findSnippetById(snippetId);

    if (snippetOptional.isEmpty()) {
      return FullResponse.create("Snippet not found", "snippet", null, HttpStatus.NOT_FOUND);
    }

    try {
      ResponseEntity<ResponseDto<Boolean>> hasPermission =
          permissionService.hasPermissionOnSnippet(userId, snippetId);

      if (Objects.requireNonNull(hasPermission.getBody()).data() != null
          || hasPermission.getBody().data().data()) {
        Snippet snippet = snippetOptional.get();

        Optional<String> content = blobStorageService.getSnippet(container, snippetId);

        if (content.isEmpty()) {
          return FullResponse.create(
              "Snippet content not found", "snippet", null, HttpStatus.NOT_FOUND);
        }

        CompleteSnippetResponseDto completeSnippetResponseDto =
            new CompleteSnippetResponseDto(
                snippet.getId(),
                snippet.getName(),
                snippet.getOwner(),
                content.get(),
                snippet.getLanguage());
        return FullResponse.create(
            "Snippet found successfully", "snippet", completeSnippetResponseDto, HttpStatus.OK);
      }
      return FullResponse.create(
          "Something went wrong getting the snippet",
          "snippet",
          null,
          HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (FeignException.Forbidden e) {
      return FullResponse.create(
          "User does not have permission to get this snippet",
          "snippet",
          null,
          HttpStatus.FORBIDDEN);
    }
  }

  // este no va a tener el content del snippet solo la data de la tabla para la UI
  public ResponseEntity<ResponseDto<List<snippetResponseDto>>> getAllSnippet(String userId) {
    ResponseEntity<ResponseDto<List<Long>>> snippetIds =
        permissionService.getAllowedSnippets(userId);

    if (snippetIds.getStatusCode().isError() || snippetIds.getBody() == null) {
      return FullResponse.create(
          "User does not have name to view snippets, because it has no snippets",
          "Snippets",
          null,
          HttpStatus.NOT_FOUND);
    }

    List<snippetResponseDto> snippets =
        snippetIds.getBody().data().data().stream()
            .map(
                snippetId ->
                    snippetRepository
                        .findSnippetById(snippetId)
                        .map(
                            snippet ->
                                new snippetResponseDto(
                                    snippet.getId(),
                                    snippet.getName(),
                                    snippet.getOwner(),
                                    snippet.getLanguage())))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();

    return FullResponse.create(
        "All snippets that has permission on", "snippetList", snippets, HttpStatus.OK);
  }

  public ResponseEntity<ResponseDto<CompleteSnippetResponseDto>> updateSnippet(
      Long id, SnippetDto snippetRequest, String userId) {
    Optional<Snippet> snippetOptional = snippetRepository.findById(id);

    if (snippetOptional.isEmpty()) {
      return FullResponse.create("Snippet not found", "snippet", null, HttpStatus.NOT_FOUND);
    }

    try {
      ResponseEntity<ResponseDto<Boolean>> hasPermission =
          permissionService.hasOwnershipPermission(userId, id);

      if (Objects.requireNonNull(hasPermission.getBody()).data() != null
          && hasPermission.getBody().data().data()) {
        Snippet snippet = snippetOptional.get();

        blobStorageService.saveSnippet(container, snippet.getId(), snippetRequest.content());

        // TODO: add async communication
        testCaseService.executeSnippetTestCases(snippet.getId());

        CompleteSnippetResponseDto completeSnippetResponseDto =
            new CompleteSnippetResponseDto(
                snippet.getId(),
                snippet.getName(),
                snippet.getOwner(),
                snippetRequest.content(),
                snippet.getLanguage());
        return FullResponse.create(
            "Snippet updated successfully", "snippet", completeSnippetResponseDto, HttpStatus.OK);
      }
      return FullResponse.create(
          "Something went wrong updating the snippet",
          "snippet",
          null,
          HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (FeignException.Forbidden e) {
      return FullResponse.create(
          "User does not have permission to update this snippet",
          "snippet",
          null,
          HttpStatus.FORBIDDEN);
    }
  }

  public ResponseEntity<ResponseDto<Long>> deleteSnippet(Long snippetId, String userId) {
    Optional<Snippet> snippetOptional = snippetRepository.findById(snippetId);

    if (snippetOptional.isEmpty()) {
      return FullResponse.create("Snippet not found", "snippetId", snippetId, HttpStatus.NOT_FOUND);
    }

    Snippet snippet = snippetOptional.get();

    try {
      ResponseEntity<ResponseDto<Long>> responseOwnership =
          permissionService.deletePermissions(snippetId, userId);

      if (responseOwnership.getStatusCode().equals(HttpStatus.OK)) {
        snippetRepository.delete(snippet);
        blobStorageService.deleteSnippet(container, snippet.getId());
        return FullResponse.create("Snippet deleted", "snippetId", snippetId, HttpStatus.OK);
      }
      return FullResponse.create(
          "Something went wrong deleting the snippet",
          "snippetId",
          snippetId,
          HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (FeignException.Forbidden e) {
      return FullResponse.create(
          "User does not have permission to delete this snippet",
          "snippet",
          null,
          HttpStatus.FORBIDDEN);
    }
  }

  public ResponseEntity<ResponseDto<Long>> shareSnippet(
      Long snippetId, String ownerId, String targetUserId) {
    try {
      return permissionService.shareSnippet(snippetId, ownerId, targetUserId);
    } catch (FeignException.Forbidden e) {
      return FullResponse.create(
          "User does not have permission to share this snippet",
          "snippet",
          null,
          HttpStatus.FORBIDDEN);
    }
  }
}
