package com.services.group4.snippet.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.services.group4.snippet.dto.snippet.request.RequestDtoSnippet;
import org.junit.jupiter.api.Test;

class RequestDtoSnippetTest {

  @Test
  void testValidRequestDtoSnippet() {
    RequestDtoSnippet dto = new RequestDtoSnippet("user123", 1L);
    assertNotNull(dto);
    assertEquals("user123", dto.userId());
    assertEquals(1L, dto.snippetId());
  }

  @Test
  void testInvalidRequestDtoSnippet() {
    Exception exception =
        assertThrows(IllegalArgumentException.class, () -> new RequestDtoSnippet(null, 1L));
    assertEquals("userId and snippetId must not be null", exception.getMessage());

    exception =
        assertThrows(IllegalArgumentException.class, () -> new RequestDtoSnippet("user123", null));
    assertEquals("userId and snippetId must not be null", exception.getMessage());
  }
}
