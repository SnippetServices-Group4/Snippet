package com.services.group4.snippet.services;

import com.services.group4.snippet.common.FullResponse;
import com.services.group4.snippet.common.states.test.TestState;
import com.services.group4.snippet.dto.snippet.response.ResponseDto;
import com.services.group4.snippet.dto.testCase.request.TestCaseRequestDto;
import com.services.group4.snippet.dto.testCase.response.TestCaseResponseDto;
import com.services.group4.snippet.dto.testCase.response.TestCaseResponseStateDto;
import com.services.group4.snippet.model.TestCase;
import com.services.group4.snippet.repositories.TestCaseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TestCaseService {

  final TestCaseRepository testCaseRepository;
  final PermissionService permissionService;


  public TestCaseService(TestCaseRepository testCaseRepository, PermissionService permissionService) {
    this.testCaseRepository = testCaseRepository;
    this.permissionService = permissionService;
  }

  public ResponseEntity<ResponseDto<TestCaseResponseDto>> createTestCase(
      TestCaseRequestDto testCaseRequestDto, String userId, Long snippetId) {

    TestCase testCase = new TestCase(testCaseRequestDto.name(), snippetId, testCaseRequestDto.inputs(), testCaseRequestDto.outputs(), TestState.NOT_STARTED);

    testCaseRepository.save(testCase);

    try {
      ResponseEntity<ResponseDto<Boolean>> hasPermission = permissionService.hasOwnershipPermission(userId, snippetId);

      if (Objects.requireNonNull(hasPermission.getBody()).data() != null
          && hasPermission.getBody().data().data()) {
        TestCaseResponseDto testCaseResponseDto =
            new TestCaseResponseDto(
                testCase.getId(),
                testCase.getName(),
                testCase.getInputs(),
                testCase.getOutputs());
        return FullResponse.create("Test case created successfully", "testCase", testCaseResponseDto, HttpStatus.CREATED);
      }
      testCaseRepository.delete(testCase);
      return FullResponse.create("Something went wrong creating the test case", "testCase", null, HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (Exception e){
      testCaseRepository.delete(testCase);
      return FullResponse.create("You don't have permission to create a test case", "testCase", null, HttpStatus.FORBIDDEN);
    }
  }

  public ResponseEntity<ResponseDto<List<TestCaseResponseStateDto>>> getTestCase(Long snippetId) {

    Optional<List<TestCase>> optionalTestCases = testCaseRepository.findTestCaseBySnippetId(snippetId);

    if (optionalTestCases.isEmpty()) {
      return FullResponse.create("Test case not found", "testCase", null, HttpStatus.NOT_FOUND);
    }

    List<TestCaseResponseStateDto> testCases = optionalTestCases.get().
        stream().map(
            testCase -> new TestCaseResponseStateDto(
                testCase.getId(), testCase.getName(),
                testCase.getState(), testCase.getInputs(),
                testCase.getOutputs())
        ).toList();

    return FullResponse.create("Test cases found successfully", "testCases", testCases, HttpStatus.OK);
  }


}