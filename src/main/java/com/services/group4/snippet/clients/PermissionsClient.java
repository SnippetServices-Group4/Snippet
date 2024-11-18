package com.services.group4.snippet.clients;

import com.services.group4.snippet.dto.snippet.request.RequestDtoShareSnippet;
import com.services.group4.snippet.dto.snippet.request.RequestDtoSnippet;
import com.services.group4.snippet.dto.snippet.response.ResponseDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "permissions", url = "${permissions.service.url}")
public interface PermissionsClient {

  @RequestMapping(
      method = RequestMethod.GET,
      value = "/ownership/permission/{userId}/for/{snippetId}")
  ResponseEntity<ResponseDto<Boolean>> hasOwnershipPermission(
      @PathVariable String userId, @PathVariable Long snippetId);

  @RequestMapping(method = RequestMethod.POST, value = "/ownership/createRelation")
  ResponseEntity<ResponseDto<Long>> addedSnippet(@RequestBody RequestDtoSnippet requestData);

  @RequestMapping(method = RequestMethod.POST, value = "/reader/share")
  ResponseEntity<ResponseDto<Long>> shareSnippet(@RequestBody RequestDtoShareSnippet requestData);

  @RequestMapping(method = RequestMethod.GET, value = "/permissions/allowedSnippets/{userId}")
  ResponseEntity<ResponseDto<List<Long>>> getAllowedSnippets(@PathVariable String userId);

  @RequestMapping(method = RequestMethod.DELETE, value = "/permissions/deleteRelation")
  ResponseEntity<ResponseDto<Long>> deletePermissions(@RequestBody RequestDtoSnippet requestData);

  @RequestMapping(method = RequestMethod.GET, value = "/permissions/{userId}/for/{snippetId}")
  ResponseEntity<ResponseDto<Boolean>> hasPermission(
      @PathVariable String userId, @PathVariable Long snippetId);
}
