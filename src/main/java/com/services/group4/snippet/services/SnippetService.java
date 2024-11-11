package com.services.group4.snippet.services;

import com.services.group4.snippet.common.FullResponse;
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

import feign.FeignException;
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
      SnippetResponseDto snippetResponseDto = new SnippetResponseDto(snippet.getId(), snippet.getName(), snippetDto.getContent(), snippet.getLanguage());
       return FullResponse.create("Snippet created successfully", "snippet", snippetResponseDto, HttpStatus.CREATED);
    }
    snippetRepository.delete(snippet);
    return FullResponse.create("Something went wrong creating the snippet", "snippet", null, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public ResponseEntity<ResponseDto<SnippetResponseDto>> getSnippet(Long snippetId, String userId) {
    Optional<Snippet> snippetOptional = this.snippetRepository.findSnippetById(snippetId);

    if (snippetOptional.isEmpty()) {
      return FullResponse.create("Snippet not found", "Snippet", null, HttpStatus.NOT_FOUND);
    }

    try {
      ResponseEntity<ResponseDto<Boolean>> hasPermission = permissionService.hasPermissionOnSnippet(userId, snippetId);

      if (Objects.requireNonNull(hasPermission.getBody()).data() != null || hasPermission.getBody().data().data()) {
        Snippet snippet = snippetOptional.get();

        // TODO: get snippet content from blob storage from infra bucket
        Optional<String> content = "contenttttt".describeConstable(); //blobStorageService.getSnippet(container, snippetId);

        if (content.isEmpty()) {
          return FullResponse.create("Snippet content not found", "Snippet", null, HttpStatus.NOT_FOUND);
        }

        SnippetResponseDto snippetResponseDto = new SnippetResponseDto(snippet.getId(), snippet.getName(), content.get(), snippet.getLanguage());
        return FullResponse.create("Snippet found successfully", "Snippet", snippetResponseDto, HttpStatus.OK);
      }
      return FullResponse.create("Something went wrong getting the snippet", "snippet", null, HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (FeignException.Forbidden e) {
      return FullResponse.create("User does not have permission to get this snippet", "snippet", null, HttpStatus.FORBIDDEN);
    }
  }

  // este no va a tener el content del snippet solo la data de la tabla para la UI
  public ResponseEntity<ResponseDto<List<AllSnippetResponseDto>>> getAllSnippet(String userId) {
    ResponseEntity<ResponseDto<List<Long>>> snippetIds = permissionService.getAllowedSnippets(userId);

    if (snippetIds.getStatusCode().isError() || snippetIds.getBody() == null) {
      return FullResponse.create("User does not have name to view snippets, because it has no snippets", "Snippets", null, HttpStatus.NOT_FOUND);
    }

    List<AllSnippetResponseDto> snippets =  snippetIds.getBody().data().data().stream()
        .map(snippetId -> snippetRepository.findSnippetById(snippetId)
            .map(snippet -> new AllSnippetResponseDto(snippet.getId(), snippet.getName(), snippet.getLanguage())))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();

    return FullResponse.create("All snippets that has permission on", "snippetList", snippets, HttpStatus.OK);
  }

  public ResponseEntity<ResponseDto<SnippetResponseDto>> updateSnippet(Long id, SnippetDto snippetRequest, String userId) {
      Optional<Snippet> snippetOptional = snippetRepository.findById(id);

      if (snippetOptional.isEmpty()) {
          return FullResponse.create("Snippet not found", "snippet", null, HttpStatus.NOT_FOUND);
      }

      try {
          ResponseEntity<ResponseDto<Boolean>> hasPermission = permissionService.updateSnippet(userId, id);

          if (Objects.requireNonNull(hasPermission.getBody()).data() != null && hasPermission.getBody().data().data()) {
              Snippet snippet = snippetOptional.get();
              snippet.setName(snippetRequest.getName());
              Language language = new Language(snippetRequest.getLanguage(), snippetRequest.getVersion());
              snippet.setLanguage(language);

              // TODO: update snippet content from blob storage infra bucket
              //blobStorageService.saveSnippet(container, snippet.getId(), snippetRequest.getContent());

              snippetRepository.save(snippet);

              SnippetResponseDto snippetResponseDto = new SnippetResponseDto(snippet.getId(), snippet.getName(), snippetRequest.getContent(), snippet.getLanguage());
              return FullResponse.create("Snippet updated successfully", "Snippet", snippetResponseDto, HttpStatus.OK);
          }
          return FullResponse.create("Something went wrong updating the snippet", "snippet", null, HttpStatus.INTERNAL_SERVER_ERROR);
      } catch (FeignException.Forbidden e) {
          return FullResponse.create("User does not have permission to update this snippet", "snippet", null, HttpStatus.FORBIDDEN);
      }
  }

  public ResponseEntity<ResponseDto<Long>> deleteSnippet(Long snippetId, String userId) {
    Optional<Snippet> snippetOptional = snippetRepository.findById(snippetId);

    if (snippetOptional.isEmpty()) {
      return FullResponse.create("Snippet not found", "snippetId", snippetId, HttpStatus.NOT_FOUND);
    }

    Snippet snippet = snippetOptional.get();

    // TODO: delete snippet content from blob storage from infra bucket
    //blobStorageService.deleteSnippet(container, snippet.getId());

    try {
      ResponseEntity<ResponseDto<Long>> responseOwnership = permissionService.deletePermissions(snippetId, userId);

      if (responseOwnership.getStatusCode().equals(HttpStatus.OK)) {
        snippetRepository.delete(snippet);
        return FullResponse.create("Snippet deleted", "snippetId", snippetId, HttpStatus.OK);
      }
      return FullResponse.create("Something went wrong deleting the snippet", "snippetId", snippetId, HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (FeignException.Forbidden e) {
      return FullResponse.create("User does not have permission to delete this snippet", "snippet", null, HttpStatus.FORBIDDEN);
    }
  }

  public ResponseEntity<ResponseDto<Long>> shareSnippet(Long snippetId, String ownerId, String targetUserId) {
    try {
      return permissionService.shareSnippet(snippetId, ownerId, targetUserId);
    } catch (FeignException.Forbidden e) {
      return FullResponse.create("User does not have permission to share this snippet", "snippet", null, HttpStatus.FORBIDDEN);
    }
  }
}
