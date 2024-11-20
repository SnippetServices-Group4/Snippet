package com.services.group4.snippet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.services.group4.snippet.DotenvConfig;
import com.services.group4.snippet.clients.PermissionsClient;
import com.services.group4.snippet.common.FullResponse;
import com.services.group4.snippet.dto.snippet.request.RequestDtoShareSnippet;
import com.services.group4.snippet.dto.snippet.request.RequestDtoSnippet;
import com.services.group4.snippet.dto.snippet.response.ResponseDto;
import com.services.group4.snippet.services.PermissionService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class PermissionServiceTests {

  @Mock private PermissionsClient permissionsClient;

  @InjectMocks private PermissionService permissionService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    DotenvConfig.loadEnv();
  }

  @Test
  void testGetAllowedSnippets() {
    when(permissionsClient.getAllowedSnippets(anyString()))
        .thenReturn(
            FullResponse.create("Allowed snippets", "data", List.of(1L, 2L, 3L), HttpStatus.OK));

    ResponseEntity<ResponseDto<List<Long>>> response =
        permissionService.getAllowedSnippets("user1");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(3, response.getBody().data().data().size());
    assertEquals(1L, response.getBody().data().data().getFirst());
    verify(permissionsClient).getAllowedSnippets("user1");
  }

  @Test
  void testHasPermissionOnSnippet() {
    when(permissionsClient.hasPermission(anyString(), anyLong()))
        .thenReturn(FullResponse.create("Permission granted", "data", true, HttpStatus.OK));

    ResponseEntity<ResponseDto<Boolean>> response =
        permissionService.hasPermissionOnSnippet("user1", 1L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().data().data());
    verify(permissionsClient).hasPermission("user1", 1L);
  }

  @Test
  void testHasOwnershipPermission() {
    when(permissionsClient.hasOwnershipPermission(anyString(), anyLong()))
        .thenReturn(FullResponse.create("Permission granted", "data", true, HttpStatus.OK));

    ResponseEntity<ResponseDto<Boolean>> response =
        permissionService.hasOwnershipPermission("user1", 1L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().data().data());
    verify(permissionsClient).hasOwnershipPermission("user1", 1L);
  }

  @Test
  void testGrantOwnerPermission() {
    when(permissionsClient.addedSnippet(any(RequestDtoSnippet.class)))
        .thenReturn(FullResponse.create("Permission granted", "data", 1L, HttpStatus.OK));

    ResponseEntity<ResponseDto<Long>> response =
        permissionService.grantOwnerPermission(1L, "user1");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1L, response.getBody().data().data());
    verify(permissionsClient).addedSnippet(any(RequestDtoSnippet.class));
  }

  @Test
  void testShareSnippet() {
    when(permissionsClient.shareSnippet(any(RequestDtoShareSnippet.class)))
        .thenReturn(FullResponse.create("Permission granted", "data", 1L, HttpStatus.OK));

    ResponseEntity<ResponseDto<Long>> response =
        permissionService.shareSnippet(1L, "user1", "user2");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1L, response.getBody().data().data());
    verify(permissionsClient).shareSnippet(any(RequestDtoShareSnippet.class));
  }

  @Test
  void testDeletePermissions() {
    // Mock del cliente
    when(permissionsClient.deletePermissions(any(RequestDtoSnippet.class)))
        .thenReturn(FullResponse.create("Permission granted", "data", 1L, HttpStatus.OK));

    // Ejecutar método
    ResponseEntity<ResponseDto<Long>> response = permissionService.deletePermissions(1L, "user1");

    // Aserciones
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1L, response.getBody().data().data());
    verify(permissionsClient).deletePermissions(any(RequestDtoSnippet.class));
  }
}
