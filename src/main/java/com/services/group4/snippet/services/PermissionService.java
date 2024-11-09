package com.services.group4.snippet.services;

import com.services.group4.snippet.clients.PermissionsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PermissionService {

  final PermissionsClient permissionsClient;

  @Autowired
  public PermissionService(PermissionsClient permissionsClient) {
    this.permissionsClient = permissionsClient;
  }

  public boolean hasPermissionOnSnippet(Long userId, Long snippetId) {
    Map<String, Object> requestData = Map.of("userId", userId, "snippetId", snippetId);
    ResponseEntity<Boolean> hasReaderPermission = permissionsClient.hasReaderPermission(requestData);
    ResponseEntity<Boolean> hasOwnerPermission = permissionsClient.hasOwnerPermission(requestData);
    if (hasReaderPermission == null || hasReaderPermission.getStatusCode().isError() ||
        hasOwnerPermission == null || hasOwnerPermission.getStatusCode().isError()) {
      // TODO: improve error handling
      return false;
    }
    return (hasReaderPermission.getBody() != null && hasReaderPermission.getBody()) ||
        (hasOwnerPermission.getBody() != null && hasOwnerPermission.getBody());
  }

  public boolean hasOwnerPermission(Long userId, Long snippetId) {
    Map<String, Object> requestData = Map.of("userId", userId, "snippetId", snippetId);
    ResponseEntity<Boolean> response = permissionsClient.hasOwnerPermission(requestData);
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

  public ResponseEntity<String> shareSnippet(Long snippetId, Long userId) {
    Map<String, Object> requestData = Map.of("userId", userId, "snippetId", snippetId);
    return permissionsClient.shareSnippet(requestData);
  }
}
