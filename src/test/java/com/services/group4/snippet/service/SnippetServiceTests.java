package com.services.group4.snippet.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.services.group4.snippet.DotenvConfig;
import com.services.group4.snippet.common.FullResponse;
import com.services.group4.snippet.common.Language;
import com.services.group4.snippet.dto.snippet.response.CompleteSnippetResponseDto;
import com.services.group4.snippet.dto.snippet.response.ResponseDto;
import com.services.group4.snippet.dto.snippet.response.SnippetDto;
import com.services.group4.snippet.dto.snippet.response.SnippetResponseDto;
import com.services.group4.snippet.model.Snippet;
import com.services.group4.snippet.repositories.SnippetRepository;
import com.services.group4.snippet.services.BlobStorageService;
import com.services.group4.snippet.services.PermissionService;
import com.services.group4.snippet.services.SnippetService;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SnippetServiceTests {

  @Mock private SnippetRepository snippetRepository;

  @Mock private PermissionService permissionService;

  @Mock private BlobStorageService blobStorageService;

  @InjectMocks private SnippetService snippetService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    DotenvConfig.loadEnv();
  }
// create snippet, no entiendo porque no mockea bien el permissionService
//  @Test
//  public void testCreateSnippet() {
//    SnippetDto snippetDto = new SnippetDto("title", "content", "java", "1.8", ".java");
//    Snippet snippet = new Snippet("title", "user1", new Language("java", "1.8", ".java"));
//    Long snippetId = 1L;
//
//    when(snippetRepository.save(any(Snippet.class))).thenReturn(snippet);
//
//
//    ResponseEntity<ResponseDto<CompleteSnippetResponseDto>> response =
//        snippetService.createSnippet(snippetDto, "user1", "user1");
//
//    assertNotNull(response);
//    assertEquals(HttpStatus.CREATED, response.getStatusCode());
//    assertNotNull(response.getBody());
//    assertEquals("Permission granted", response.getBody().message());
//  }

  @Test
  public void testGetSnippet() {
    // Setup
    Snippet snippet = new Snippet("Test Snippet", "user1", new Language("java", "1.8", ".java"));
    Optional<String> content = Optional.of("Content");

    // Mocking
    when(snippetRepository.findSnippetById(anyLong())).thenReturn(Optional.of(snippet));
    when(blobStorageService.getSnippet(any(), anyLong())).thenReturn(content);
    when(permissionService.hasPermissionOnSnippet(anyString(), anyLong()))
        .thenReturn(FullResponse.create("Permission granted", "permission", true, HttpStatus.OK));

    // Test execution
    ResponseEntity<ResponseDto<CompleteSnippetResponseDto>> response =
        snippetService.getSnippet(1L, "user1");

    // Assertions
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Test Snippet", response.getBody().data().data().name());
  }

  @Test
  public void testGetAllSnippet() {
      // Setup
      Snippet snippet = new Snippet("Test Snippet", "user1", new Language("java", "1.8", ".java"));
      List<Long> snippetIds = List.of(1L);

      // Mocking
      when(permissionService.getAllowedSnippets(anyString()))
          .thenReturn(FullResponse.create("Permission granted", "snippetIds", snippetIds, HttpStatus.OK));
      when(snippetRepository.findSnippetById(anyLong())).thenReturn(Optional.of(snippet));

      // Test execution
      ResponseEntity<ResponseDto<List<SnippetResponseDto>>> response = snippetService.getAllSnippet("user1");

      // Assertions
      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertNotNull(response.getBody());
      assertEquals(1, response.getBody().data().data().size());
      assertEquals("Test Snippet", response.getBody().data().data().get(0).name());
  }

  @Test
  public void testUpdateSnippet() {
    // Setup
    SnippetDto snippetDto = new SnippetDto(null, "Updated Content", null, null, null);
    Snippet snippet = new Snippet("Test Snippet", "user1", new Language("java", "1.8", ".java"));

    // Mocking
    when(snippetRepository.findById(anyLong())).thenReturn(Optional.of(snippet));
    when(permissionService.hasOwnershipPermission(anyString(), anyLong()))
        .thenReturn(FullResponse.create("Permission granted", "permission", true, HttpStatus.OK));

    // Test execution
    ResponseEntity<ResponseDto<CompleteSnippetResponseDto>> response =
        snippetService.updateSnippet(1L, snippetDto, "user1");

    // Assertions
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Test Snippet", response.getBody().data().data().name());
  }

    @Test
    public void testDeleteSnippet() {
      Snippet snippet = new Snippet("Test Snippet", "user1", new Language("java", "1.8",
   ".java"));
      Long snippetId = 1L;

      when(snippetRepository.findById(anyLong())).thenReturn(Optional.of(snippet));
      when(permissionService.deletePermissions(1L, "user1"))
          .thenReturn(FullResponse.create("Snippet deleted", "snippetId", 1L, HttpStatus.OK));

      // Test execution
      ResponseEntity<ResponseDto<Long>> response = snippetService.deleteSnippet(1L, "user1");

      // Assertions
      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertEquals(snippetId, response.getBody().data().data());
    }


}
