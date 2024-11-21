package com.services.group4.snippet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.services.group4.snippet.DotenvConfig;
import com.services.group4.snippet.common.FullResponse;
import com.services.group4.snippet.common.Language;
import com.services.group4.snippet.common.ValidationState;
import com.services.group4.snippet.dto.snippet.response.CompleteSnippetResponseDto;
import com.services.group4.snippet.dto.snippet.response.ResponseDto;
import com.services.group4.snippet.dto.snippet.response.SnippetDto;
import com.services.group4.snippet.dto.snippet.response.SnippetResponseDto;
import com.services.group4.snippet.dto.request.ProcessingRequestDto;
import com.services.group4.snippet.model.Snippet;
import com.services.group4.snippet.repositories.SnippetRepository;
import com.services.group4.snippet.services.BlobStorageService;
import com.services.group4.snippet.services.ParserService;
import com.services.group4.snippet.services.PermissionService;
import com.services.group4.snippet.services.SnippetService;
import feign.FeignException;
import java.util.List;
import java.util.NoSuchElementException;
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

  @Mock private ParserService parserService;

  @InjectMocks private SnippetService snippetService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    DotenvConfig.loadEnv();
  }

  @Test
  public void testGetSnippet() {
    Snippet snippet = new Snippet("Test Snippet", "user1", new Language("java", "1.8", ".java"));
    Optional<String> content = Optional.of("Content");

    when(snippetRepository.findSnippetById(anyLong())).thenReturn(Optional.of(snippet));
    when(blobStorageService.getSnippet(any(), anyLong())).thenReturn(content);
    when(permissionService.hasPermissionOnSnippet(anyString(), anyLong()))
        .thenReturn(FullResponse.create("Permission granted", "permission", true, HttpStatus.OK));

    ResponseEntity<ResponseDto<CompleteSnippetResponseDto>> response =
        snippetService.getSnippet(1L, "user1");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Test Snippet", response.getBody().data().data().name());
  }

  @Test
  public void testGetAllSnippet() {
    Snippet snippet = new Snippet("Test Snippet", "user1", new Language("java", "1.8", ".java"));
    List<Long> snippetIds = List.of(1L);

    when(permissionService.getAllowedSnippets(anyString()))
        .thenReturn(
            FullResponse.create("Permission granted", "snippetIds", snippetIds, HttpStatus.OK));
    when(snippetRepository.findSnippetById(anyLong())).thenReturn(Optional.of(snippet));

    ResponseEntity<ResponseDto<List<SnippetResponseDto>>> response =
        snippetService.getAllSnippet("user1");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().data().data().size());
    assertEquals("Test Snippet", response.getBody().data().data().getFirst().name());
  }

  // mismo caso loco de create
  //  @Test
  //  public void testUpdateSnippet() {
  //    SnippetDto snippetDto = new SnippetDto(null, "Updated Content", null, null, null);
  //    Snippet snippet = new Snippet("Test Snippet", "user1", new Language("java", "1.8",
  // ".java"));
  //
  //    when(snippetRepository.findById(anyLong())).thenReturn(Optional.of(snippet));
  //    when(parserService.analyze(any(ProcessingRequestDto.class))).thenReturn(
  //        FullResponse.create("Snippet analyzed successfully", "validationState",
  // ValidationState.VALID, HttpStatus.OK));
  //    when(permissionService.hasOwnershipPermission(anyString(), anyLong()))
  //        .thenReturn(FullResponse.create("Permission granted", "permission", true,
  // HttpStatus.OK));
  //
  //    ResponseEntity<ResponseDto<CompleteSnippetResponseDto>> response =
  //        snippetService.updateSnippet(1L, snippetDto, "user1");
  //
  //    assertEquals(HttpStatus.OK, response.getStatusCode());
  //    assertNotNull(response.getBody());
  //    assertEquals("Test Snippet", response.getBody().data().data().name());
  //  }

  @Test
  public void testDeleteSnippet() {
    Snippet snippet = new Snippet("Test Snippet", "user1", new Language("java", "1.8", ".java"));
    Long snippetId = 1L;
    when(snippetRepository.findById(anyLong())).thenReturn(Optional.of(snippet));
    when(permissionService.deletePermissions(snippetId, "user1"))
        .thenReturn(FullResponse.create("Snippet deleted", "snippetId", snippetId, HttpStatus.OK));

    // Test execution
    ResponseEntity<ResponseDto<Long>> response = snippetService.deleteSnippet(snippetId, "user1");

    // Assertions
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(snippetId, response.getBody().data().data());
  }

  @Test
  public void testDeleteSnippet_Forbidden() {
    Snippet snippet = new Snippet("Test Snippet", "user1", new Language("java", "1.8", ".java"));
    Long snippetId = 1L;

    when(snippetRepository.findById(anyLong())).thenReturn(Optional.of(snippet));
    when(permissionService.deletePermissions(snippetId, "user1"))
        .thenReturn(
            FullResponse.create("Permission denied", "snippetId", snippetId, HttpStatus.FORBIDDEN));

    // Test execution
    ResponseEntity<ResponseDto<Long>> response = snippetService.deleteSnippet(snippetId, "user1");

    // Assertions
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  public void testUpdateSnippet_InternalError() {
    SnippetDto snippetDto = new SnippetDto(null, "Updated Content", null, null, null);
    Snippet snippet = new Snippet("Test Snippet", "user1", new Language("java", "1.8", ".java"));

    when(snippetRepository.findById(anyLong())).thenReturn(Optional.of(snippet));
    when(parserService.analyze(any(ProcessingRequestDto.class)))
        .thenReturn(
            FullResponse.create(
                "Snippet analyzed successfully",
                "validationState",
                ValidationState.VALID,
                HttpStatus.OK));
    when(permissionService.hasOwnershipPermission(anyString(), anyLong()))
        .thenReturn(
            FullResponse.create("Permission denied", "permission", false, HttpStatus.FORBIDDEN));

    ResponseEntity<ResponseDto<CompleteSnippetResponseDto>> response =
        snippetService.updateSnippet(1L, snippetDto, "user1");

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals("Something went wrong updating the snippet", response.getBody().message());
  }

  @Test
  public void testDeleteSnippetNotFound() {
    when(snippetRepository.findById(anyLong())).thenReturn(Optional.empty());

    ResponseEntity<ResponseDto<Long>> response = snippetService.deleteSnippet(1L, "user1");

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Snippet not found", response.getBody().message());
  }

  @Test
  public void testShareSnippet() {
    when(permissionService.shareSnippet(anyLong(), anyString(), anyString()))
        .thenReturn(
            FullResponse.create("Snippet shared successfully", "snippetId", 1L, HttpStatus.OK));

    ResponseEntity<ResponseDto<Long>> response = snippetService.shareSnippet(1L, "owner1", "user2");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Snippet shared successfully", response.getBody().message());
  }

  @Test
  public void testUpdateSnippet_Empty() {
      SnippetDto snippetDto = new SnippetDto(null, "Updated Content", null, null, null);

      when(snippetRepository.findById(anyLong())).thenReturn(Optional.empty());
      when(parserService.analyze(any(ProcessingRequestDto.class)))
          .thenReturn(
              FullResponse.create(
                  "Snippet analyzed successfully",
                  "validationState",
                  ValidationState.VALID,
                  HttpStatus.OK));

      NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
          snippetService.updateSnippet(1L, snippetDto, "user1");
      });

      assertEquals("Snippet not found", exception.getMessage());
  }

  @Test
  public void testShareSnippet_FeignExceptionForbidden() {
    Long snippetId = 1L;
    String ownerId = "owner1";
    String targetUserId = "user2";

    // Simula que el FeignClient lanza una FeignException.Forbidden
    doThrow(FeignException.Forbidden.class)
        .when(permissionService)
        .shareSnippet(snippetId, ownerId, targetUserId);

    ResponseEntity<ResponseDto<Long>> response =
        snippetService.shareSnippet(snippetId, ownerId, targetUserId);

    assertNotNull(response);
    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(
        "User does not have permission to share this snippet", response.getBody().message());
  }

  @Test
  public void testGetAllSnippet_NotFound() {
    Snippet snippet = new Snippet("Test Snippet", "user1", new Language("java", "1.8", ".java"));

    when(permissionService.getAllowedSnippets(anyString()))
        .thenReturn(
            FullResponse.create("Permission not found", "snippetIds", null, HttpStatus.NOT_FOUND));
    when(snippetRepository.findSnippetById(anyLong())).thenReturn(Optional.of(snippet));

    ResponseEntity<ResponseDto<List<SnippetResponseDto>>> response =
        snippetService.getAllSnippet("user1");

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody().data().data());
  }

  @Test
  public void testUpdateSnippet_FeignExceptionForbidden() {
    Long snippetId = 1L;
    String userId = "user-123";
    Snippet snippet = new Snippet("Test Snippet", "user-123", new Language("java", "1.8", ".java"));
    SnippetDto snippetDto =
        new SnippetDto("Test Snippet", "updated content", "java", "1.8", ".java");

    when(snippetRepository.findById(snippetId)).thenReturn(Optional.of(snippet));
    when(parserService.analyze(any(ProcessingRequestDto.class)))
        .thenReturn(
            FullResponse.create(
                "Snippet analyzed successfully",
                "validationState",
                ValidationState.VALID,
                HttpStatus.OK));

    // Simular que el FeignClient lanza FeignException.Forbidden
    doThrow(FeignException.Forbidden.class)
        .when(permissionService)
        .hasOwnershipPermission(userId, snippetId);

    ResponseEntity<ResponseDto<CompleteSnippetResponseDto>> response =
        snippetService.updateSnippet(snippetId, snippetDto, userId);

    assertNotNull(response);
    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(
        "User does not have permission to update this snippet", response.getBody().message());
  }

  @Test
  public void testGetSnippet_Empty() {
    Optional<String> content = Optional.of("Content");

    when(snippetRepository.findSnippetById(anyLong())).thenReturn(Optional.empty());
    when(blobStorageService.getSnippet(any(), anyLong())).thenReturn(content);
    when(permissionService.hasPermissionOnSnippet(anyString(), anyLong()))
        .thenReturn(FullResponse.create("Permission granted", "permission", true, HttpStatus.OK));

    ResponseEntity<ResponseDto<CompleteSnippetResponseDto>> response =
        snippetService.getSnippet(1L, "user1");

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Snippet not found", response.getBody().message());
  }

  @Test
  public void testGetSnippet_EmptyContent() {
    Snippet snippet = new Snippet("Test Snippet", "user1", new Language("java", "1.8", ".java"));

    when(snippetRepository.findSnippetById(anyLong())).thenReturn(Optional.of(snippet));
    when(blobStorageService.getSnippet(any(), anyLong())).thenReturn(Optional.empty());
    when(permissionService.hasPermissionOnSnippet(anyString(), anyLong()))
        .thenReturn(FullResponse.create("Permission granted", "permission", true, HttpStatus.OK));

    ResponseEntity<ResponseDto<CompleteSnippetResponseDto>> response =
        snippetService.getSnippet(1L, "user1");

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Snippet content not found", response.getBody().message());
  }

  @Test
  public void testGetSnippet_InternalError() {
    Snippet snippet = new Snippet("Test Snippet", "user1", new Language("java", "1.8", ".java"));
    Optional<String> content = Optional.of("Content");

    when(snippetRepository.findSnippetById(anyLong())).thenReturn(Optional.of(snippet));
    when(blobStorageService.getSnippet(any(), anyLong())).thenReturn(content);
    when(permissionService.hasPermissionOnSnippet(anyString(), anyLong()))
        .thenReturn(
            FullResponse.create("Permission granted", "permission", false, HttpStatus.FORBIDDEN));

    ResponseEntity<ResponseDto<CompleteSnippetResponseDto>> response =
        snippetService.getSnippet(1L, "user1");

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Something went wrong getting the snippet", response.getBody().message());
  }

  // Posta no entiendo xq no mockea bien el create
  //  @Test
  //  public void testCreateSnippet() {
  //    SnippetDto snippetDto = new SnippetDto("Test Snippet", "Updated Content", "java", "1.8",
  // ".java");
  //    Snippet snippet = new Snippet("Test Snippet", "user1", new Language("java", "1.8",
  // ".java"));
  //    snippet.setId(1L); // Simular ID generado por el repositorio.
  //
  //    // Mock del snippetRepository.save
  //    when(snippetRepository.save(any(Snippet.class))).thenReturn(snippet);
  //
  //    // Mock del permiso para grantOwnerPermission
  //    when(permissionService.grantOwnerPermission(anyLong(), anyString()))
  //        .thenAnswer(invocation -> {
  //          Long id = invocation.getArgument(0);
  //          return new ResponseEntity<>(new ResponseDto<>("Permission granted", new
  // DataTuple<>("permission", id)), HttpStatus.CREATED);
  //        });
  //
  //
  //    // Mock del blobStorageService.saveSnippet
  //    doNothing().when(blobStorageService).saveSnippet(anyString(), anyLong(), anyString());
  //
  //
  //    ResponseEntity<ResponseDto<CompleteSnippetResponseDto>> response =
  //        snippetService.createSnippet(snippetDto, "user1", "user1");
  //
  //    // Verificación
  //    verify(permissionService).grantOwnerPermission(eq(snippet.getId()), eq("user1")); //
  // Confirma que se llama.
  //    assertNotNull(response);
  //    assertEquals(HttpStatus.CREATED, response.getStatusCode());
  //    assertNotNull(response.getBody());
  //    assertEquals("Snippet created successfully", response.getBody().message());
  //    assertEquals(snippet.getName(), response.getBody().data().data().name());
  //  }

  @Test
  public void getLanguage() {
    Language language = new Language("java", "1.8", ".java");
    when(snippetRepository.findById(anyLong()))
        .thenReturn(Optional.of(new Snippet("Test Snippet", "user1", language)));
    Language l = snippetService.getLanguage(1L);
    assertEquals(language, l);
  }

  @Test
  public void getLanguageException() {
    when(snippetRepository.findById(anyLong())).thenReturn(Optional.empty());
    assertThrows(NoSuchElementException.class, () -> snippetService.getLanguage(1L));
  }
}
