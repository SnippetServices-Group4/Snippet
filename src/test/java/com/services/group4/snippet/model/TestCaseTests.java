package com.services.group4.snippet.model;

import com.services.group4.snippet.DotenvConfig;
import com.services.group4.snippet.common.states.test.TestState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestCaseTests {

  @BeforeAll
  public static void setupEnv() {
    DotenvConfig.loadEnv();
  }

  @Test
  public void testTestCaseCreation() {
    List<String> inputs = List.of("input1", "input2");
    List<String> outputs = List.of("output1", "output2");
    TestState state = TestState.PASSED;
    TestCase testCase = new TestCase("Test Case", 1L, inputs, outputs, state);

    assertNotNull(testCase);
    assertEquals("Test Case", testCase.getName());
    assertEquals(1L, testCase.getSnippetId());
    assertEquals(inputs, testCase.getInputs());
    assertEquals(outputs, testCase.getOutputs());
    assertEquals(state, testCase.getState());
  }
}
