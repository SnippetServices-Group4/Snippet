package com.services.group4.snippet.repositories;

import com.services.group4.snippet.model.TestCase;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
  @Query("SELECT t FROM TestCase t WHERE t.snippetId = :snippetId")
  Optional<List<TestCase>> findTestCaseBySnippetId(Long snippetId);
}
