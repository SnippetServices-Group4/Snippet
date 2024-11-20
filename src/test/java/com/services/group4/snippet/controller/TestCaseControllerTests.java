package com.services.group4.snippet.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.snippet.DotenvConfig;
import com.services.group4.snippet.common.FullResponse;
import com.services.group4.snippet.common.states.test.TestState;
import com.services.group4.snippet.dto.testcase.request.TestCaseRequestDto;
import com.services.group4.snippet.dto.testcase.response.TestCaseResponseDto;
import com.services.group4.snippet.dto.testcase.response.TestCaseResponseStateDto;
import com.services.group4.snippet.services.TestCaseService;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TestCaseController.class)
class TestCaseControllerTests {

  @BeforeAll
  public static void setupEnv() {
    DotenvConfig.loadEnv();
  }

  @MockBean private TestCaseService testCaseService;

  @Autowired private MockMvc mockMvc;

  @Test
  public void testCreateTestCase() throws Exception {
    TestCaseResponseDto testCaseResponseDto =
        new TestCaseResponseDto(
            1L, "Test Case", List.of("Description"), List.of("Expected Output"));
    when(testCaseService.createTestCase(any(TestCaseRequestDto.class), anyString(), anyLong()))
        .thenReturn(
            FullResponse.create(
                "Test case created successfully", "testCase", testCaseResponseDto, HttpStatus.OK));

    TestCaseRequestDto testCaseRequestDto =
        new TestCaseRequestDto(List.of("Description"), List.of("Expected Output"), "Test Case");
    mockMvc
        .perform(
            post("/testCase/createFor/1")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(testCaseRequestDto))
                .header("userId", "user1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Test case created successfully"))
        .andExpect(jsonPath("$.data.testCase.name").value("Test Case"));
  }

  @Test
  public void testGetAllTestCases() throws Exception {
    List<TestCaseResponseStateDto> testCaseList =
        List.of(
            new TestCaseResponseStateDto(
                1L,
                "Test Case 1",
                TestState.NOT_STARTED,
                List.of("Description 1"),
                List.of("Expected Output 1")),
            new TestCaseResponseStateDto(
                2L,
                "Test Case 2",
                TestState.NOT_STARTED,
                List.of("Description 2"),
                List.of("Expected Output 2")));

    when(testCaseService.getTestCase(anyLong()))
        .thenReturn(
            FullResponse.create(
                "All test cases for snippet", "testCaseList", testCaseList, HttpStatus.OK));

    mockMvc
        .perform(get("/testCase/getAll/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("All test cases for snippet"))
        .andExpect(jsonPath("$.data.testCaseList[0].name").value("Test Case 1"))
        .andExpect(jsonPath("$.data.testCaseList[1].name").value("Test Case 2"));
  }

  @Test
  public void testUpdateTestCase() throws Exception {
    TestCaseResponseStateDto testCaseResponseStateDto =
        new TestCaseResponseStateDto(
            1L,
            "Test Case",
            TestState.NOT_STARTED,
            List.of("Description"),
            List.of("Expected Output"));
    when(testCaseService.updateTestCase(
            any(TestCaseRequestDto.class), anyString(), anyLong(), anyLong()))
        .thenReturn(
            FullResponse.create(
                "Test case updated successfully",
                "testCase",
                testCaseResponseStateDto,
                HttpStatus.OK));

    TestCaseRequestDto testCaseRequestDto =
        new TestCaseRequestDto(List.of("Description"), List.of("Expected Output"), "Test Case");
    mockMvc
        .perform(
            put("/testCase/update/1/for/1")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(testCaseRequestDto))
                .header("userId", "user1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Test case updated successfully"))
        .andExpect(jsonPath("$.data.testCase.name").value("Test Case"));
  }

  @Test
  public void testDeleteTestCase() throws Exception {
    when(testCaseService.deleteTestCase(anyString(), anyLong(), anyLong()))
        .thenReturn(
            FullResponse.create("Test case deleted", "testCase", "Test Case 1", HttpStatus.OK));

    mockMvc
        .perform(delete("/testCase/delete/1/for/1").header("userId", "user1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Test case deleted"))
        .andExpect(jsonPath("$.data.testCase").value("Test Case 1"));
  }
}
