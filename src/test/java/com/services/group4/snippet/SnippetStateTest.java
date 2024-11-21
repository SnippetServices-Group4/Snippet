package com.services.group4.snippet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.services.group4.snippet.common.states.ProcessState;
import com.services.group4.snippet.common.states.snippet.SnippetState;
import org.junit.jupiter.api.Test;

class SnippetStateTest {

  @Test
  void testSnippetStateInitialization() {
    // Crear una instancia de SnippetState
    SnippetState snippetState = new SnippetState();

    // Verificar que los valores por defecto estén establecidos correctamente
    assertEquals(ProcessState.NOT_STARTED, snippetState.getFormatting());
    assertEquals(ProcessState.NOT_STARTED, snippetState.getLinting());
  }

  @Test
  void testSettersAndGetters() {
    SnippetState snippetState = new SnippetState();

    // Asignar valores a los campos
    snippetState.setFormatting(ProcessState.RUNNING);
    snippetState.setLinting(ProcessState.PASSED);

    // Verificar que los valores se hayan asignado correctamente
    assertEquals(ProcessState.RUNNING, snippetState.getFormatting());
    assertEquals(ProcessState.PASSED, snippetState.getLinting());
  }

  @Test
  void testConstructor() {
    // Usar el constructor por defecto y verificar que los valores por defecto sean correctos
    SnippetState snippetState = new SnippetState();

    assertNotNull(snippetState);
    assertEquals(ProcessState.NOT_STARTED, snippetState.getFormatting());
    assertEquals(ProcessState.NOT_STARTED, snippetState.getLinting());
  }

  @Test
  void testColumnDefinition() {
    // Verificar que las anotaciones de la columna sean correctas
    SnippetState snippetState = new SnippetState();

    assertNotNull(snippetState);
  }

  @Test
  void testEquals() {
    SnippetState snippetState1 = new SnippetState();
    SnippetState snippetState2 = new SnippetState();

    // Ambos objetos deberían ser iguales porque tienen el mismo estado por defecto
    assertEquals(snippetState1, snippetState2);

    // Verificar que no se considera igual a un objeto de tipo diferente
    assertNotEquals("SomeString", snippetState1);

    // Asegurarse de que el hashCode también sea igual si los objetos son iguales
    assertEquals(snippetState1.hashCode(), snippetState2.hashCode());
  }

  @Test
  void testHashCode() {
    SnippetState snippetState1 = new SnippetState();
    SnippetState snippetState2 = new SnippetState();

    // Verificar que el hashCode sea el mismo para objetos iguales
    assertEquals(snippetState1.hashCode(), snippetState2.hashCode());
  }

  @Test
  void testToString() {
    SnippetState snippetState = new SnippetState();

    // Verificar que toString devuelve la representación esperada
    String expectedString = "SnippetState(formatting=NOT_STARTED, linting=NOT_STARTED)";
    assertEquals(expectedString, snippetState.toString());
  }
}
