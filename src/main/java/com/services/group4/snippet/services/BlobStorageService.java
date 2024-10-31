package com.services.group4.snippet.services;

import com.services.group4.snippet.clients.BucketClient;
import java.util.List;
import java.util.Optional;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Generated
@Service
public class BlobStorageService {

  private final BucketClient bucketClient;

  @Autowired
  public BlobStorageService(BucketClient bucketClient) {
    this.bucketClient = bucketClient;
  }

  public void saveSnippet(String container, Long id, String content) {
    bucketClient.saveSnippet(container, id, content);
  }

  public Optional<String> getSnippet(String container, Long id) {
    ResponseEntity<String> response = bucketClient.getSnippet(container, id);
    if (response.hasBody()) {
      return Optional.ofNullable(response.getBody());
    }
    return Optional.empty();
  }

  public void deleteSnippet(String container, Long id) {
    bucketClient.deleteSnippet(container, id);
  }

  public Optional<List<String>> getAllSnippets(String container) {
    ResponseEntity<List<String>> response = bucketClient.getAllSnippets(container);
    if (response.hasBody()) {
      return Optional.ofNullable(response.getBody());
    }
    return Optional.empty();
  }
}
