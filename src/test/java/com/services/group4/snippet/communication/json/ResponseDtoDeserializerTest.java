package com.services.group4.snippet.communication.json;

import static org.junit.jupiter.api.Assertions.*;

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

    ResponseDto<List<Long>> responseDto = objectMapper.readValue(json, ResponseDto.class);

    assertNotNull(responseDto);
    assertEquals("Test message", responseDto.message());
    assertNotNull(responseDto.data());
    assertEquals("snippetList", responseDto.data().name());
    assertTrue(responseDto.data().data() instanceof List);
    assertEquals(3, ((List<Long>) responseDto.data().data()).size());
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

    ResponseDto<List<Long>> responseDto = objectMapper.readValue(json, ResponseDto.class);

    assertNotNull(responseDto);
    assertEquals("Test message", responseDto.message());
    assertNotNull(responseDto.data());
    assertEquals("snippetList", responseDto.data().name());
    assertTrue(responseDto.data().data() instanceof List);
    assertEquals(3, ((List<Long>) responseDto.data().data()).size());
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

    ResponseDto<List<Long>> responseDto = objectMapper.readValue(json, ResponseDto.class);

    assertNotNull(responseDto);
    assertEquals("Test message", responseDto.message());
    assertNotNull(responseDto.data());
    assertEquals("snippetId", responseDto.data().name());
  }
}
