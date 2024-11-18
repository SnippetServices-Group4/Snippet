package com.services.group4.snippet.controller.test;

import com.services.group4.snippet.model.CommunicationMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
  @GetMapping("/")
  public CommunicationMessage snippetCommunication() {
    return new CommunicationMessage("Snippet", "Snippet is up and running!");
  }

  @GetMapping("/parser/communication")
  public CommunicationMessage testParserCommunication() {
    return new CommunicationMessage("Snippet", "Communication from Parser to Snippet works!");
  }

  @GetMapping("/permission/communication")
  public CommunicationMessage testPermissionCommunication() {
    return new CommunicationMessage("Snippet", "Communication from Permission to Snippet works!");
  }
}
