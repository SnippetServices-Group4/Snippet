package com.services.group4.snippet.common.states;

public enum ProcessState {
  NOT_STARTED("not_started"),
  RUNNING("running"),
  PASSED("passed"),
  FAILED("failed");

  public final String name;

  ProcessState(String name) {
    this.name = name;
  }
}
