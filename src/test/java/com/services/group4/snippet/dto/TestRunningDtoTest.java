package com.services.group4.snippet.dto;

import com.services.group4.snippet.dto.request.TestRunningDto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class TestRunningDtoTest {

  @Test
  void testValidTestRunningDto() {
    // Datos de prueba
    List<String> inputs = List.of("input1", "input2");
    List<String> outputs = List.of("output1", "output2");
    String language = "java";
    String version = "1.8";

    // Crear el objeto TestRunningDto
    TestRunningDto dto = new TestRunningDto(inputs, outputs, language, version);

    // Verificar que el objeto no sea null y los valores sean correctos
    assertNotNull(dto);
    assertEquals(inputs, dto.inputs());
    assertEquals(outputs, dto.outputs());
    assertEquals(language, dto.language());
    assertEquals(version, dto.version());
  }
}

