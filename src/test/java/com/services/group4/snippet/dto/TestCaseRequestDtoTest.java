package com.services.group4.snippet.dto;

import com.services.group4.snippet.dto.request.TestCaseRequestDto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class TestCaseRequestDtoTest {

  @Test
  void testValidTestCaseRequestDto() {
    // Datos de prueba
    List<String> inputs = List.of("input1", "input2");
    List<String> outputs = List.of("output1", "output2");
    String name = "Test Case 1";

    // Crear el objeto TestCaseRequestDto
    TestCaseRequestDto dto = new TestCaseRequestDto(inputs, outputs, name);

    // Verificar que el objeto no sea null y los valores sean correctos
    assertNotNull(dto);
    assertEquals(inputs, dto.inputs());
    assertEquals(outputs, dto.outputs());
    assertEquals(name, dto.name());
  }
}

