package com.services.group4.snippet.model;

import com.services.group4.snippet.common.states.test.TestState;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;
import lombok.Generated;

@Generated
@Entity
@Data
public class TestCase {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "snippet_id")
  private Snippet snippet;

  @NotNull private String name;

  @ElementCollection @NotNull private List<String> inputs;

  @ElementCollection @NotNull private List<String> outputs;

  @Column @NotNull private TestState state;

  public TestCase() {}

  public TestCase(
      String name, Snippet snippet, List<String> inputs, List<String> outputs, TestState state) {
    this.snippet = snippet;
    this.name = name;
    this.inputs = inputs;
    this.outputs = outputs;
    this.state = state;
  }
}
