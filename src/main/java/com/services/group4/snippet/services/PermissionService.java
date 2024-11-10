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

  public ResponseEntity<ResponseDto<List<Long>>> getAllowedSnippets(String userId) {
    return permissionsClient.getAllowedSnippets(userId);
  }

  public ResponseEntity<ResponseDto<Boolean>> hasPermissionOnSnippet(String userId, Long snippetId) {
    return permissionsClient.hasPermission(userId, snippetId);
  }

  public ResponseEntity<ResponseDto<Boolean>>  updateSnippet(String userId, Long snippetId) {
    return permissionsClient.updateSnippet(userId, snippetId);
  }

  public ResponseEntity<ResponseDto<Long>> grantOwnerPermission(Long snippetId, String userId) {
    Map<String, Object> requestData = Map.of("userId", userId, "snippetId", snippetId);
    return permissionsClient.addedSnippet(requestData);
  }

  public ResponseEntity<ResponseDto<Long>> shareSnippet(Long snippetId, String ownerId, String targetUserId) {
    Map<String, Object> requestData = Map.of("ownerId", ownerId, "snippetId", snippetId, "targetUserId", targetUserId);
    return permissionsClient.shareSnippet(requestData);
  }

  public ResponseEntity<ResponseDto<Long>> deletePermissions(Long snippetId, String userId) {
    Map<String, Object> requestData = Map.of("userId", userId, "snippetId", snippetId);
    return permissionsClient.deletePermissions(requestData);
  }
}
