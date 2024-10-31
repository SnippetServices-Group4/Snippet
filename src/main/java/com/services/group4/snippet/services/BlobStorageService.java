package com.services.group4.snippet.services;

import java.util.Optional;

import com.services.group4.snippet.clients.BucketClient;
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

    public void uploadSnippet(String container, String name, String content) {
        bucketClient.saveSnippet(container, name, content);
    }

    public Optional<String> getSnippet(String container, String name) {
        ResponseEntity<String> response = bucketClient.getSnippet(container, name);
        if (response.hasBody()) {
            return Optional.of(response.getBody());
        }
        return Optional.empty();
    }

    public void deleteSnippet(String container, String name) {
        bucketClient.deleteSnippet(container, name);
    }
}