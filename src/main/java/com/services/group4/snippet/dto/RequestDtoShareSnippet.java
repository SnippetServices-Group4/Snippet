package com.services.group4.snippet.dto;

public record RequestDtoShareSnippet(String userId, Long snippetId, String targetUserId) {
  public RequestDtoShareSnippet {
    if (userId == null || snippetId == null) {
      throw new IllegalArgumentException("userId and snippetId must not be null");
    }
  }
}
