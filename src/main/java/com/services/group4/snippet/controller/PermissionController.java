package com.services.group4.snippet.controller;

import com.services.group4.snippet.model.CommunicationMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PermissionController {
  @GetMapping("/test/permission/communication")
  public CommunicationMessage testPermissionCommunication() {
    return new CommunicationMessage("Snippet", "Communication between Snippet and Permission works!");
  }
}
