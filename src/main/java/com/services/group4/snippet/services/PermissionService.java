package com.services.group4.snippet.services;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class PermissionService {

  private final WebClient webClient;

  public PermissionService(WebClient.Builder webClientBuilder) {
    this.webClient =
        webClientBuilder
            .baseUrl("http://permission-service:8081")
            .build(); // La URL del servicio Permission
  }

  public ResponseEntity<?> createOwnership(Long userId, Long snippetId) {
    Map<String, Object> requestBody = Map.of("userId", userId, "snippetId", snippetId);

    // Enviar solicitud a Permission para crear el ownership
    return webClient
        .post()
        .uri("/ownership/create2")
        .bodyValue(requestBody)
        .retrieve()
        .toEntity(String.class) // Asumimos que devuelve un String en el cuerpo, ajustable
        .block();
  }
}
