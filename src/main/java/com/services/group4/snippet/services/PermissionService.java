package com.services.group4.snippet.services;

import com.services.group4.snippet.clients.PermissionsClient;
import com.services.group4.snippet.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class PermissionService {

  final PermissionsClient permissionsClient;

  @Autowired
  public PermissionService(PermissionsClient permissionsClient) {
    this.permissionsClient = permissionsClient;
  }

  public ResponseEntity<ResponseDto<List<Long>>> getAllowedSnippets(Long userId) {
    return permissionsClient.getAllowedSnippets(userId);
  }

  public ResponseEntity<ResponseDto<Boolean>> hasPermissionOnSnippet(Long userId, Long snippetId) {
    return permissionsClient.hasPermission(userId, snippetId);
  }

  public boolean updateSnippet(Long userId, Long snippetId) {
    ResponseEntity<ResponseDto<Boolean>> response = permissionsClient.updateSnippet(userId, snippetId);
    return Boolean.TRUE.equals(response.getBody().data());
  }

  public ResponseEntity<ResponseDto<Long>> grantOwnerPermission(Long snippetId, Long userId) {
    Map<String, Object> requestData = Map.of("userId", userId, "snippetId", snippetId);
    return permissionsClient.addedSnippet(requestData);
  }

  public ResponseEntity<ResponseDto<Long>> shareSnippet(Long snippetId, Long ownerId, Long targetUserId) {
    Map<String, Object> requestData = Map.of("ownerId", ownerId, "snippetId", snippetId, "targetUserId", targetUserId);
    return permissionsClient.shareSnippet(requestData);
  }

  public ResponseEntity<ResponseDto<Long>> deletePermissions(Long snippetId, Long userId) {
    Map<String, Object> requestData = Map.of("userId", userId, "snippetId", snippetId);
    return permissionsClient.deletePermissions(requestData);
  }
}
