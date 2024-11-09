package com.services.group4.snippet.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

@FeignClient(value = "permissions", url = "http://localhost:8081")
public interface PermissionsClient {

  //TODO: chequear la ruta de ownership y reader
  @RequestMapping(method = RequestMethod.GET, value = "/reader/getPermission")
  ResponseEntity<Boolean> hasReaderPermission(@RequestBody Map<String, Object> requestData);

  @RequestMapping(method = RequestMethod.GET, value = "/ownership/getPermission")
  ResponseEntity<Boolean> hasOwnerPermission(@RequestBody Map<String, Object> requestData);

  @RequestMapping(method = RequestMethod.POST, value = "/ownership/created")
  ResponseEntity<String> addedSnippet(@RequestBody Map<String, Object> requestData);

  @RequestMapping(method = RequestMethod.POST, value = "/reader/share")
  ResponseEntity<String> shareSnippet(@RequestBody Map<String, Object> requestData);

  @RequestMapping(method = RequestMethod.GET, value = "/reader/getAllowedSnippets")
  ResponseEntity<List<Long>> getAllowedSnippets(@RequestBody Long userId);
}
