package com.services.group4.snippet.services;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PermissionService {
  private RestTemplate restTemplate;

  @Configuration
  static class TestConfig {
    @Bean
    public RestTemplate restTemplate() {
      return new RestTemplate();
    }
  }

  public ResponseEntity<?> createOwnership(Long userId, Long snippetId) {
    String url = "http://localhost:8081/ownership/create2";
    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("userId", userId);
    requestBody.put("snippetId", snippetId);

    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json");

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

    try {
      ResponseEntity<?> response = restTemplate.postForEntity(url, entity, Object.class);
      return new ResponseEntity<>(response.getBody(), response.getStatusCode());
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>("Error creating ownership", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}