package com.services.group4.snippet.common;

public enum PermissionType {
  OWNER("owner"),
  SHARED("shared");

  public final String name;

  PermissionType(String name) {
    this.name = name;
  }
}
