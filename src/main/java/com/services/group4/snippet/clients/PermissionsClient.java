package com.services.group4.snippet.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

// TODO: check if the url is correct
@FeignClient(value = "permissions", url = "http://permissions:8080/")
public interface PermissionsClient {

  @RequestMapping(method = RequestMethod.GET, value = "/permissions/")
  ResponseEntity<Boolean> hasPermission(
      @RequestParam("type") String type, @RequestParam("snippetId") Long snippetId);

  @RequestMapping(method = RequestMethod.POST, value = "permissions/create")
  ResponseEntity<Void> addSnippet(@RequestParam("snippetId") Long snippetId, @RequestParam("userId") Long userId);
}
