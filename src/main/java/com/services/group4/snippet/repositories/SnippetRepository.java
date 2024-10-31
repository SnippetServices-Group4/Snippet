package com.services.group4.snippet.repositories;

import com.services.group4.snippet.model.Snippet;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnippetRepository extends JpaRepository<Snippet, Long> {
  Optional<Snippet> findSnippetById(Long id);

  Optional<Snippet> findSnippetByTitle(String title);
}
