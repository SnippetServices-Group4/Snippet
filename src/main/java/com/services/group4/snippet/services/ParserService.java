package com.services.group4.snippet.services;

import com.services.group4.snippet.clients.ParserClient;
import com.services.group4.snippet.common.ValidationState;
import com.services.group4.snippet.common.states.test.TestState;
import com.services.group4.snippet.dto.request.ProcessingRequestDto;
import com.services.group4.snippet.dto.request.TestRunningDto;
import com.services.group4.snippet.dto.snippet.response.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ParserService {
  private final ParserClient parserClient;

  @Autowired
  public ParserService(ParserClient parserClient) {
    this.parserClient = parserClient;
  }

  public ResponseEntity<ResponseDto<TestState>> runTest(
      TestRunningDto forwardedRequest, Long snippetId) {
    return parserClient.runTest(forwardedRequest, snippetId);
  }

  public ResponseEntity<ResponseDto<ValidationState>> analyze(ProcessingRequestDto request) {
    return parserClient.analyze(request);
  }
}
