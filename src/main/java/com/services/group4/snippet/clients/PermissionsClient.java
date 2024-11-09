package com.services.group4.snippet.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

@FeignClient(value = "permissions", url = "http://localhost:8081")
public interface PermissionsClient {

  @RequestMapping(method = RequestMethod.GET, value = "/reader/permission/{userId}/for/{snippetId}")
  ResponseEntity<Boolean> hasReaderPermission(@PathVariable Long userId, @PathVariable Long snippetId);

  @RequestMapping(method = RequestMethod.GET, value = "/ownership/permission/{userId}/for/{snippetId}")
  ResponseEntity<Boolean> hasOwnerPermission(@PathVariable Long userId, @PathVariable Long snippetId);

  @RequestMapping(method = RequestMethod.POST, value = "/ownership/created")
  ResponseEntity<String> addedSnippet(@RequestBody Map<String, Object> requestData);

  @RequestMapping(method = RequestMethod.POST, value = "/reader/share")
  ResponseEntity<String> shareSnippet(@RequestBody Map<String, Object> requestData);

  @RequestMapping(method = RequestMethod.GET, value = "/reader/getAllowedSnippets/{userId}")
  ResponseEntity<List<Long>> getAllowedSnippets(@PathVariable Long userId);
}
