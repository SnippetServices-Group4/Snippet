package com.services.group4.snippet.controller;

import com.services.group4.snippet.model.CommunicationMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
  @GetMapping("/parser/communication")
  public CommunicationMessage testParserCommunication() {
    return new CommunicationMessage("Snippet", "Communication between Snippet and Parser works!");
  }

  @GetMapping("/permission/communication")
  public CommunicationMessage testPermissionCommunication() {
    return new CommunicationMessage("Snippet", "Communication between Snippet and Permission works!");
  }
}
