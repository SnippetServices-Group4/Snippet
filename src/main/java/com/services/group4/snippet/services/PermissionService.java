package com.services.group4.snippet.services;

import com.services.group4.snippet.clients.PermissionsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PermissionService {

  final PermissionsClient permissionsClient;

  @Autowired
  public PermissionService(PermissionsClient permissionsClient) {
    this.permissionsClient = permissionsClient;
  }

  public ResponseEntity<List<Long>> getAllowedSnippets(Long userId) {
    ResponseEntity<List<Long>> response = permissionsClient.getAllowedSnippets(userId);
    if (response == null || response.getStatusCode().isError()) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return response;
  }

  public boolean hasPermissionOnSnippet(Long userId, Long snippetId) {
    ResponseEntity<Boolean> hasOwnerPermission = permissionsClient.hasOwnerPermission(userId, snippetId);
    ResponseEntity<Boolean> hasReaderPermission = permissionsClient.hasReaderPermission(userId, snippetId);
    if (hasReaderPermission == null || hasReaderPermission.getStatusCode().isError() ||
        hasOwnerPermission == null || hasOwnerPermission.getStatusCode().isError()) {
      // TODO: improve error handling
      return false;
    }
    return (hasReaderPermission.getBody() != null && hasReaderPermission.getBody()) ||
        (hasOwnerPermission.getBody() != null && hasOwnerPermission.getBody());
  }

  public boolean hasOwnerPermission(Long userId, Long snippetId) {
    ResponseEntity<Boolean> response = permissionsClient.hasOwnerPermission(userId, snippetId);
    if (response == null || response.getStatusCode().isError()) {
      // TODO: improve error handling
      return false;
    }
    return Boolean.TRUE.equals(response.getBody()) && !response.getStatusCode().isError();
  }

  public ResponseEntity<String> grantOwnerPermission(Long snippetId, Long userId) {
    Map<String, Object> requestData = Map.of("userId", userId, "snippetId", snippetId);
    return permissionsClient.addedSnippet(requestData);
  }

  public ResponseEntity<String> shareSnippet(Long snippetId, Long ownerId, Long targetUserId) {
    Map<String, Object> requestData = Map.of("ownerId", ownerId, "snippetId", snippetId, "targetUserId", targetUserId);
    return permissionsClient.shareSnippet(requestData);
  }
}
