package com.services.group4.snippet.services;

import com.services.group4.snippet.clients.PermissionsClient;
import com.services.group4.snippet.common.PermissionType;
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

  public boolean hasPermissionOnSnippet(PermissionType type, Long snippetId) {
    ResponseEntity<Boolean> response = permissionsClient.hasPermission(type.name, snippetId);
    if (response == null || response.getStatusCode().isError()) {
      // TODO
      return false;
    }
    return response.getBody() != null && response.getBody();
  }

  public void grantOwnerPermission(Long snippetId) {
    permissionsClient.addSnippet(snippetId);
  }


  // TODO: take it right from the token
  public String getUsername(String token) {

    return "user 1";
  }
}
