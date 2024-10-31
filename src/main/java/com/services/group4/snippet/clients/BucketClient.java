package com.services.group4.snippet.clients;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "asset-api", url = "http://asset_service:8080/v1")
public interface BucketClient {

  @GetMapping("/asset/{container}/{key}")
  ResponseEntity<String> getSnippet(
      @PathVariable("container") String container, @PathVariable("key") Long key);

  @PutMapping("/asset/{container}/{key}")
  ResponseEntity<Void> saveSnippet(
      @PathVariable("container") String container,
      @PathVariable("key") Long key,
      @RequestBody String content);

  @DeleteMapping("/asset/{container}/{key}")
  ResponseEntity<Void> deleteSnippet(
      @PathVariable("container") String container, @PathVariable("key") Long key);
}
