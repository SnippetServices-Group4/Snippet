package com.services.group4.snippet.services;

import com.services.group4.snippet.clients.ParserClient;
import com.services.group4.snippet.dto.testCase.request.TestRunningDto;
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

    public ResponseEntity<ResponseDto<Object>> runTest(TestRunningDto forwardedRequest) {
        return parserClient.runTest(forwardedRequest);
    }
}
