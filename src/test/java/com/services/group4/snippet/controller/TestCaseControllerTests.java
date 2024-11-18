package com.services.group4.snippet.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.snippet.DotenvConfig;
import com.services.group4.snippet.common.FullResponse;
import com.services.group4.snippet.common.states.test.TestState;
import com.services.group4.snippet.dto.testCase.response.TestCaseResponseDto;
import com.services.group4.snippet.dto.testCase.response.TestCaseResponseStateDto;
import com.services.group4.snippet.dto.testCase.request.TestCaseRequestDto;
import com.services.group4.snippet.services.TestCaseService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestCaseControllerTests {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private TestCaseService testCaseService;

  private TestCaseRequestDto testCaseRequestDto;
  private TestCaseResponseDto testCaseResponseDto;
  private TestCaseResponseStateDto testCaseResponseStateDto;

  @BeforeAll
  public static void setupEnv() {
    DotenvConfig.loadEnv();
  }

  @BeforeEach
  public void setup() {
    testCaseRequestDto = new TestCaseRequestDto(List.of("input1"), List.of("output1"), "Test Case");
    testCaseResponseDto = new TestCaseResponseDto(1L, "Test Case", List.of("input1"), List.of("output1"));
    testCaseResponseStateDto = new TestCaseResponseStateDto(1L, "Test Case", TestState.NOT_STARTED, List.of("input1"), List.of("output1"));
  }

  @Test
  public void testCreateTestCase() throws Exception {
    when(testCaseService.createTestCase(any(TestCaseRequestDto.class), anyString(), anyLong()))
        .thenReturn(FullResponse.create("Test case created successfully", "testCase", testCaseResponseDto, HttpStatus.CREATED));

    mockMvc
        .perform(post("/testCase/createFor/1")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(testCaseRequestDto))
            .header("userId", "user1"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.message").value("Test case created successfully"))
        .andExpect(jsonPath("$.data.testCase.name").value("Test Case"));
  }

  @Test
  public void testGetAllTestCases() throws Exception {
    List<TestCaseResponseStateDto> testCaseList = List.of(
        new TestCaseResponseStateDto(1L, "Test Case 1", TestState.NOT_STARTED, List.of("input1"), List.of("output1")),
        new TestCaseResponseStateDto(2L, "Test Case 2", TestState.NOT_STARTED, List.of("input2"), List.of("output2"))
    );

    when(testCaseService.getTestCase(anyLong()))
        .thenReturn(FullResponse.create("Test cases found successfully", "testCases", testCaseList, HttpStatus.OK));

    mockMvc
        .perform(get("/testCase/getAll/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Test cases found successfully"))
        .andExpect(jsonPath("$.data.testCases[0].name").value("Test Case 1"))
        .andExpect(jsonPath("$.data.testCases[1].name").value("Test Case 2"));
  }

  @Test
  public void testUpdateTestCase() throws Exception {
    when(testCaseService.updateTestCase(any(TestCaseRequestDto.class), anyString(), anyLong(), anyLong()))
        .thenReturn(FullResponse.create("Test case updated successfully", "testCase", testCaseResponseStateDto, HttpStatus.OK));

    mockMvc
        .perform(put("/testCase/update/1/for/1")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(testCaseRequestDto))
            .header("userId", "user1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Test case updated successfully"))
        .andExpect(jsonPath("$.data.testCase.name").value("Test Case"));
  }

  @Test
  public void testDeleteTestCase() throws Exception {
    when(testCaseService.deleteTestCase(anyString(), anyLong(), anyLong()))
        .thenReturn(FullResponse.create("Test case deleted successfully", "testCase", testCaseResponseDto.name(), HttpStatus.OK));

    mockMvc
        .perform(delete("/testCase/delete/1/for/1")
            .header("userId", "user1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Test case deleted successfully"))
        .andExpect(jsonPath("$.data.testCase").value("Test Case"));
  }
}