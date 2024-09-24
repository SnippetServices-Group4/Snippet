package com.services.group4.snippet.utils;

import org.springframework.web.client.RestTemplate;

public class ModuleChecker {
  public static boolean isPermissionModuleRunning() {
    try {
      RestTemplate restTemplate = new RestTemplate();
      restTemplate.getForObject("http://localhost:8081/test/snippet/communication", String.class);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public static boolean isParserModuleRunning() {
    try {
      RestTemplate restTemplate = new RestTemplate();
      restTemplate.getForObject("http://localhost:8082/test/snippet/communication", String.class);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
