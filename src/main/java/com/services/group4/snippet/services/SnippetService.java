package com.services.group4.snippet.services;

import com.services.group4.snippet.dto.SnippetDto;
import com.services.group4.snippet.model.Snippet;
import com.services.group4.snippet.repositories.SnippetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class SnippetService {
    private final String container = "snippets";
    final SnippetRepository snippetRepository;
    final BlobStorageService blobStorageService;

    @Autowired
    public SnippetService(SnippetRepository snippetRepository, BlobStorageService blobStorageService) {
        this.snippetRepository = snippetRepository;
        this.blobStorageService = blobStorageService;
    }


    public Snippet createSnippet(SnippetDto snippetDto) {
        blobStorageService.uploadSnippet(container, snippetDto.getName(), snippetDto.getContent());
        Snippet snippet = new Snippet(snippetDto.getName(), snippetDto.getOwner());
        return snippetRepository.save(snippet);
    }

    public Optional<String> getSnippet(Long snippetId) {
        Optional<Snippet> snippetOptional = this.snippetRepository.findSnippetById(snippetId);
        if (snippetOptional.isPresent()) {
            Snippet snippet = snippetOptional.get();
            String name = snippet.getName();
            return blobStorageService.getSnippet(container, name);
        }
        return Optional.empty();
    }

}
