package com.services.group4.snippet.services;

import com.services.group4.snippet.dto.SnippetRequest;
import com.services.group4.snippet.model.Snippet;

public class SnippetService {

  public SnippetRequest convertToDTO(Snippet snippet) {
    return new SnippetRequest(snippet.getTitle(), snippet.getContent(), snippet.getVersion(), snippet.getLanguage());
  }

  public static Snippet convertToEntity(SnippetRequest snippetDTO) {
    return new Snippet(snippetDTO.getTitle(), snippetDTO.getContent(), snippetDTO.getVersion(), snippetDTO.getLanguage());
  }
}
