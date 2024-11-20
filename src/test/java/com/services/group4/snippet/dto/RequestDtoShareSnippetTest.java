package com.services.group4.snippet.dto;

import static org.junit.jupiter.api.Assertions.*;

import com.services.group4.snippet.dto.snippet.request.RequestDtoShareSnippet;
import org.junit.jupiter.api.Test;

class RequestDtoShareSnippetTest {

  @Test
  void testValidRequestDtoShareSnippet() {
    RequestDtoShareSnippet dto = new RequestDtoShareSnippet("user123", 1L, "targetUser456");
    assertNotNull(dto);
    assertEquals("user123", dto.userId());
    assertEquals(1L, dto.snippetId());
    assertEquals("targetUser456", dto.targetUserId());
  }

  @Test
  void testInvalidRequestDtoShareSnippet() {
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              new RequestDtoShareSnippet(null, 1L, "targetUser456");
            });
    assertEquals("userId and snippetId must not be null", exception.getMessage());

    exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              new RequestDtoShareSnippet("user123", null, "targetUser456");
            });
    assertEquals("userId and snippetId must not be null", exception.getMessage());
  }
}
