package com.services.group4.snippet.services;

import com.services.group4.snippet.clients.PermissionsClient;
import com.services.group4.snippet.dto.snippet.request.RequestDtoShareSnippet;
import com.services.group4.snippet.dto.snippet.request.RequestDtoSnippet;
import com.services.group4.snippet.dto.snippet.response.ResponseDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

  public ResponseEntity<ResponseDto<Boolean>> hasPermissionOnSnippet(
      String userId, Long snippetId) {
    return permissionsClient.hasPermission(userId, snippetId);
  }

  public ResponseEntity<ResponseDto<Boolean>> updateSnippet(String userId, Long snippetId) {
    return permissionsClient.updateSnippet(userId, snippetId);
  }

  public ResponseEntity<ResponseDto<Long>> grantOwnerPermission(Long snippetId, String userId) {
    RequestDtoSnippet requestDto = new RequestDtoSnippet(userId, snippetId);
    return permissionsClient.addedSnippet(requestDto);
  }

  public ResponseEntity<ResponseDto<Long>> shareSnippet(
      Long snippetId, String ownerId, String targetUserId) {
    RequestDtoShareSnippet requestDto =
        new RequestDtoShareSnippet(ownerId, snippetId, targetUserId);

    return permissionsClient.shareSnippet(requestDto);
  }

  public ResponseEntity<ResponseDto<Long>> deletePermissions(Long snippetId, String userId) {
    RequestDtoSnippet requestDto = new RequestDtoSnippet(userId, snippetId);

    return permissionsClient.deletePermissions(requestDto);
  }
}
