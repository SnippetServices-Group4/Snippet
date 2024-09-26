package com.services.group4.snippet.repository;

import com.services.group4.snippet.model.Snippet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SnippetRepository extends JpaRepository<Snippet, Long> {
  Optional<Snippet> findByTitle(String title);
}
