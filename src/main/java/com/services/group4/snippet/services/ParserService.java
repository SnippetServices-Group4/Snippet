package com.services.group4.snippet.services;

import com.services.group4.snippet.clients.ParsersClient;
import com.services.group4.snippet.common.ValidationState;
import com.services.group4.snippet.dto.snippet.response.ResponseDto;
import com.services.group4.snippet.dto.testCase.request.ProcessingRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ParserService {
  final ParsersClient parsersClient;

  @Autowired
  public ParserService(ParsersClient parsersClient) {
    this.parsersClient = parsersClient;
  }

  public ResponseEntity<ResponseDto<ValidationState>> analyze(ProcessingRequestDto request) {
    return parsersClient.analyze(request);
  }
}
