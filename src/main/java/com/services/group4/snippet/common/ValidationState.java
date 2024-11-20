package com.services.group4.snippet.common;

public enum ValidationState {
  VALID("VALID"),
  INVALID("INVALID");

  public final String valid;

  ValidationState(String valid) {
    this.valid = valid;
  }
}
