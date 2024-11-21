package com.services.group4.snippet.clients;

import com.services.group4.snippet.common.ValidationState;
import com.services.group4.snippet.common.states.test.TestState;
import com.services.group4.snippet.dto.request.ProcessingRequestDto;
import com.services.group4.snippet.dto.request.TestRunningDto;
import com.services.group4.snippet.dto.snippet.response.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "parser", url = "${parser.service.url}")
public interface ParserClient {
  @RequestMapping(method = RequestMethod.POST, value = "/parsers/runTest/{snippetId}")
  ResponseEntity<ResponseDto<TestState>> runTest(
      @RequestBody TestRunningDto testRequest, @PathVariable Long snippetId);

  @RequestMapping(method = RequestMethod.POST, value = "/parsers/validate")
  ResponseEntity<ResponseDto<ValidationState>> analyze(@RequestBody ProcessingRequestDto snippet);
}
