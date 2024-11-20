package com.services.group4.snippet.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.snippet.dto.snippet.response.ResponseDto;
import java.util.List;
import org.junit.jupiter.api.Test;

class ResponseDtoDeserializerTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void testDeserialize_ValidJson() throws Exception {
    String json =
        """
            {
                "message": "Test message",
                "data": {
                    "name": "snippetList",
                    "data": [1, 2, 3]
                }
            }
        """;

    ResponseDto<List<Long>> responseDto = objectMapper.readValue(json, new TypeReference<>() {});

    assertNotNull(responseDto);
    assertEquals("Test message", responseDto.message());
    assertNotNull(responseDto.data());
    assertEquals("snippetList", responseDto.data().name());
    assertInstanceOf(List.class, responseDto.data().data());
    assertEquals(3, (responseDto.data().data()).size());
  }

  @Test
  void testDeserialize_ValidJsonList() throws Exception {
    String json =
        """
            {
                "message": "Test message",
                "data": {
                    "snippetList": [1, 2, 3]
                }
            }
        """;

    ResponseDto<List<Long>> responseDto = objectMapper.readValue(json, new TypeReference<>() {});

    assertNotNull(responseDto);
    assertEquals("Test message", responseDto.message());
    assertNotNull(responseDto.data());
    assertEquals("snippetList", responseDto.data().name());
    assertInstanceOf(List.class, responseDto.data().data());
    assertEquals(3, (responseDto.data().data()).size());
  }

  @Test
  void testDeserialize_ValidJsonChanges() throws Exception {
    String json =
        """
            {
                "message": "Test message",
                "data": {
                    "snippetId": 1
                }
            }
        """;

    ResponseDto<List<Long>> responseDto = objectMapper.readValue(json, new TypeReference<>() {});

    assertNotNull(responseDto);
    assertEquals("Test message", responseDto.message());
    assertNotNull(responseDto.data());
    assertEquals("snippetId", responseDto.data().name());
  }
}
