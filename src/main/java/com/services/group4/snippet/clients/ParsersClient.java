package com.services.group4.snippet.clients;

import com.services.group4.snippet.common.ValidationState;
import com.services.group4.snippet.dto.snippet.response.ResponseDto;
import com.services.group4.snippet.dto.testCase.request.ProcessingRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "parser", url = "${PARSER_SERVICE_URL}")
public interface ParsersClient {

  @RequestMapping(method = RequestMethod.POST, value = "/parsers/validate")
  ResponseEntity<ResponseDto<ValidationState>> analyze(@RequestBody ProcessingRequestDto snippet);
}
