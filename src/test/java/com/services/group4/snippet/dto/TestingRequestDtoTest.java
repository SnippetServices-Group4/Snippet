package com.services.group4.snippet.dto;

import com.services.group4.snippet.dto.request.TestingRequestDto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class TestingRequestDtoTest {

  @Test
  void testValidTestingRequestDtoWithTestId() {
    // Datos de prueba
    String testId = "test123";
    List<String> inputs = List.of("input1", "input2");
    List<String> outputs = List.of("output1", "output2");

    // Crear el objeto TestingRequestDto
    TestingRequestDto dto = new TestingRequestDto(testId, inputs, outputs);

    // Verificar que el objeto no sea null y los valores sean correctos
    assertNotNull(dto);
    assertEquals(testId, dto.testId());
    assertEquals(inputs, dto.inputs());
    assertEquals(outputs, dto.outputs());
  }

  @Test
  void testValidTestingRequestDtoWithoutTestId() {
    // Datos de prueba
    List<String> inputs = List.of("input1", "input2");
    List<String> outputs = List.of("output1", "output2");

    // Crear el objeto TestingRequestDto sin testId
    TestingRequestDto dto = new TestingRequestDto(null, inputs, outputs);

    // Verificar que el objeto no sea null y los valores sean correctos
    assertNotNull(dto);
    assertNull(dto.testId());
    assertEquals(inputs, dto.inputs());
    assertEquals(outputs, dto.outputs());
  }

  @Test
  void testInvalidTestingRequestDtoWithNullInputs() {
    // Intentar crear un dto con inputs null
    Exception exception = assertThrows(IllegalArgumentException.class, () -> new TestingRequestDto("test123", null, List.of("output1")));
    assertEquals("inputs must not be null", exception.getMessage());
  }

  @Test
  void testInvalidTestingRequestDtoWithNullOutputs() {
    // Intentar crear un dto con outputs null
    Exception exception = assertThrows(IllegalArgumentException.class, () -> new TestingRequestDto("test123", List.of("input1"), null));
    assertEquals("outputs must not be null", exception.getMessage());
  }
}

