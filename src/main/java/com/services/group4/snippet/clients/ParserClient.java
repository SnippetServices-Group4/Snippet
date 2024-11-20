package com.services.group4.snippet.clients;

import com.services.group4.snippet.dto.testCase.request.TestRunningDto;
import com.services.group4.snippet.dto.snippet.response.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "parser", url = "${parser.service.url}")
public interface ParserClient {
    @RequestMapping(method = RequestMethod.POST, value = "/parsers/runTest")
    ResponseEntity<ResponseDto<Object>> runTest(@RequestBody TestRunningDto testRequest);
}
