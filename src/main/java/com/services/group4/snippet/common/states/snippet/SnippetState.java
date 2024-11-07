package com.services.group4.snippet.common.states.snippet;

import com.services.group4.snippet.common.states.ProcessState;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Embeddable
@Data
public class SnippetState {

  @NotNull
  @Column(columnDefinition = "INTEGER")
  private ProcessState formatting = ProcessState.NOT_STARTED;

  @NotNull
  @Column(columnDefinition = "INTEGER")
  private ProcessState linting = ProcessState.NOT_STARTED;

  public SnippetState() {}
}
