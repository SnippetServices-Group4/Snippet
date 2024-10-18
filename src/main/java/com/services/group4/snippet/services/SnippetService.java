package com.services.group4.snippet.services;

import com.services.group4.snippet.dto.SnippetDto;
import com.services.group4.snippet.model.Snippet;
import com.services.group4.snippet.repositories.SnippetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SnippetService {
    final SnippetRepository snippetRepository;

    @Autowired
    public SnippetService(SnippetRepository snippetRepository) {
        this.snippetRepository = snippetRepository;
    }


    public Snippet createSnippet(SnippetDto snippetDto) {
        Snippet snippet = new Snippet(snippetDto.getName(), snippetDto.getOwner());
        return snippetRepository.save(snippet);
    }
}
