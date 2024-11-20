package com.services.group4.snippet.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.services.group4.snippet.DotenvConfig;
import com.services.group4.snippet.common.FullResponse;
import com.services.group4.snippet.common.states.test.TestState;
import com.services.group4.snippet.dto.snippet.response.ResponseDto;
import com.services.group4.snippet.dto.testCase.request.TestCaseRequestDto;
import com.services.group4.snippet.dto.testCase.response.TestCaseResponseDto;
import com.services.group4.snippet.dto.testCase.response.TestCaseResponseStateDto;
import com.services.group4.snippet.model.TestCase;
import com.services.group4.snippet.repositories.TestCaseRepository;
import com.services.group4.snippet.services.PermissionService;
import com.services.group4.snippet.services.TestCaseService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class TestCaseServiceTests {

  @Mock private TestCaseRepository testCaseRepository;

  @Mock private PermissionService permissionService;

  @InjectMocks private TestCaseService testCaseService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    DotenvConfig.loadEnv();
  }

  @Test
  void testCreateTestCase_Success() {
    TestCaseRequestDto requestDto =
        new TestCaseRequestDto(List.of("Input"), List.of("Output"), "Test Name");
    TestCase testCase =
        new TestCase("Test Name", 1L, List.of("Input"), List.of("Output"), TestState.NOT_STARTED);

    when(permissionService.hasOwnershipPermission("user1", 1L))
        .thenReturn(FullResponse.create("Permission granted", "permission", true, HttpStatus.OK));
    when(testCaseRepository.save(any(TestCase.class))).thenReturn(testCase);

    // Ejecución
    ResponseEntity<ResponseDto<TestCaseResponseDto>> response =
        testCaseService.createTestCase(requestDto, "user1", 1L);

    // Validación
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Test Name", response.getBody().data().data().name());
  }

  @Test
  void testCreateTestCase_Forbidden() {
    TestCaseRequestDto requestDto =
        new TestCaseRequestDto(List.of("Input"), List.of("Output"), "Test Name");

    when(permissionService.hasOwnershipPermission("user1", 1L))
        .thenReturn(ResponseEntity.status(HttpStatus.FORBIDDEN).body(null));

    ResponseEntity<ResponseDto<TestCaseResponseDto>> response =
        testCaseService.createTestCase(requestDto, "user1", 1L);

    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    assertNull(response.getBody().data().data());
  }

  @Test
  void testCreateTestCase_InternalError() {
    TestCaseRequestDto requestDto =
        new TestCaseRequestDto(List.of("Input"), List.of("Output"), "Test Name");

    when(permissionService.hasOwnershipPermission("user1", 1L))
        .thenReturn(
            FullResponse.create(
                "Something went wrong creating the test case",
                "permission",
                false,
                HttpStatus.INTERNAL_SERVER_ERROR));

    ResponseEntity<ResponseDto<TestCaseResponseDto>> response =
        testCaseService.createTestCase(requestDto, "user1", 1L);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertNull(response.getBody().data().data());
    assertEquals("Something went wrong creating the test case", response.getBody().message());
  }

  @Test
  void testGetTestCase_Success() {
    TestCase testCase1 =
        new TestCase("Test Name 1", 1L, List.of("Input 1"), List.of("Output 1"), TestState.PASSED);
    TestCase testCase2 =
        new TestCase(
            "Test Name 2", 1L, List.of("Input 2"), List.of("Output 2"), TestState.NOT_STARTED);

    when(testCaseRepository.findTestCaseBySnippetId(1L))
        .thenReturn(Optional.of(List.of(testCase1, testCase2)));

    ResponseEntity<ResponseDto<List<TestCaseResponseStateDto>>> response =
        testCaseService.getTestCase(1L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(2, response.getBody().data().data().size());
  }

  @Test
  void testGetTestCase_NotFound() {
    when(testCaseRepository.findTestCaseBySnippetId(1L)).thenReturn(Optional.empty());

    ResponseEntity<ResponseDto<List<TestCaseResponseStateDto>>> response =
        testCaseService.getTestCase(1L);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody().data().data());
  }

  @Test
  void testDeleteTestCase_Success() {
    TestCase testCase =
        new TestCase("Test Name", 1L, List.of("Input"), List.of("Output"), TestState.NOT_STARTED);

    when(testCaseRepository.findById(1L)).thenReturn(Optional.of(testCase));
    when(permissionService.hasOwnershipPermission("user1", 1L))
        .thenReturn(FullResponse.create("Permission granted", "permission", true, HttpStatus.OK));

    ResponseEntity<ResponseDto<String>> response = testCaseService.deleteTestCase("user1", 1L, 1L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Test case deleted successfully", response.getBody().message());
  }

  @Test
  void testDeleteTestCase_Forbidden() {
    TestCase testCase =
        new TestCase("Test Name", 1L, List.of("Input"), List.of("Output"), TestState.NOT_STARTED);

    when(testCaseRepository.findById(1L)).thenReturn(Optional.of(testCase));
    when(permissionService.hasOwnershipPermission("user1", 1L))
        .thenReturn(
            FullResponse.create("Permission denied", "permission", false, HttpStatus.FORBIDDEN));

    ResponseEntity<ResponseDto<String>> response = testCaseService.deleteTestCase("user1", 1L, 1L);

    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    assertEquals("You don't have permission to delete the test case", response.getBody().message());
  }

  @Test
  void testDeleteTestCase_InternalServerError() {
    TestCase testCase =
        new TestCase("Test Name", 1L, List.of("Input"), List.of("Output"), TestState.NOT_STARTED);

    when(testCaseRepository.findById(1L)).thenReturn(Optional.of(testCase));
    when(permissionService.hasOwnershipPermission("user1", 1L))
        .thenReturn(
            FullResponse.create(
                "Permission denied", "permission", false, HttpStatus.INTERNAL_SERVER_ERROR));

    ResponseEntity<ResponseDto<String>> response = testCaseService.deleteTestCase("user1", 1L, 1L);

    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    assertEquals("You don't have permission to delete the test case", response.getBody().message());
  }

  @Test
  void testDeleteTestCase_NotFound() {
    when(testCaseRepository.findById(1L)).thenReturn(Optional.empty());

    ResponseEntity<ResponseDto<String>> response = testCaseService.deleteTestCase("user1", 1L, 1L);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void testUpdateTestCase_Success() {
    TestCaseRequestDto requestDto =
        new TestCaseRequestDto(List.of("New Input"), List.of("New Output"), "Updated Name");

    TestCase existingTestCase =
        new TestCase(
            "Old Name", 1L, List.of("Old Input"), List.of("Old Output"), TestState.NOT_STARTED);

    TestCase updatedTestCase =
        new TestCase(
            "Updated Name", 1L, List.of("New Input"), List.of("New Output"), TestState.NOT_STARTED);

    when(permissionService.hasOwnershipPermission("user1", 1L))
        .thenReturn(FullResponse.create("Permission granted", "permission", true, HttpStatus.OK));
    when(testCaseRepository.findById(1L)).thenReturn(Optional.of(existingTestCase));
    when(testCaseRepository.save(any(TestCase.class))).thenReturn(updatedTestCase);

    ResponseEntity<ResponseDto<TestCaseResponseStateDto>> response =
        testCaseService.updateTestCase(requestDto, "user1", 1L, 1L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Updated Name", response.getBody().data().data().name());
    assertEquals(List.of("New Input"), response.getBody().data().data().inputs());
    assertEquals(List.of("New Output"), response.getBody().data().data().outputs());
  }

  @Test
  void testUpdateTestCase_PermissionDenied() {
    TestCaseRequestDto requestDto =
        new TestCaseRequestDto(List.of("New Input"), List.of("New Output"), "Updated Name");

    TestCase existingTestCase =
        new TestCase(
            "Old Name", 1L, List.of("Old Input"), List.of("Old Output"), TestState.NOT_STARTED);

    TestCase updatedTestCase =
        new TestCase(
            "Updated Name", 1L, List.of("New Input"), List.of("New Output"), TestState.NOT_STARTED);

    when(permissionService.hasOwnershipPermission("user1", 1L))
        .thenReturn(
            FullResponse.create("Permission denied", "permission", false, HttpStatus.FORBIDDEN));
    when(testCaseRepository.findById(1L)).thenReturn(Optional.of(existingTestCase));
    when(testCaseRepository.save(any(TestCase.class))).thenReturn(updatedTestCase);

    ResponseEntity<ResponseDto<TestCaseResponseStateDto>> response =
        testCaseService.updateTestCase(requestDto, "user1", 1L, 1L);

    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void testUpdateTestCase_InternalServerError() {
    TestCaseRequestDto requestDto =
        new TestCaseRequestDto(List.of("New Input"), List.of("New Output"), "Updated Name");

    TestCase existingTestCase =
        new TestCase(
            "Old Name", 1L, List.of("Old Input"), List.of("Old Output"), TestState.NOT_STARTED);

    TestCase updatedTestCase =
        new TestCase(
            "Updated Name", 1L, List.of("New Input"), List.of("New Output"), TestState.NOT_STARTED);

    when(permissionService.hasOwnershipPermission("user1", 1L))
        .thenReturn(
            FullResponse.create(
                "Permission denied", "permission", false, HttpStatus.INTERNAL_SERVER_ERROR));
    when(testCaseRepository.findById(1L)).thenReturn(Optional.of(existingTestCase));
    when(testCaseRepository.save(any(TestCase.class))).thenReturn(updatedTestCase);

    ResponseEntity<ResponseDto<TestCaseResponseStateDto>> response =
        testCaseService.updateTestCase(requestDto, "user1", 1L, 1L);

    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void testUpdateTestCase_NotFound() {
    TestCaseRequestDto requestDto =
        new TestCaseRequestDto(List.of("Input"), List.of("Output"), "Test Name");

    ResponseEntity<ResponseDto<TestCaseResponseStateDto>> response =
        testCaseService.updateTestCase(requestDto, "user1", 1L, 1L);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody().data().data());
  }
}
