package com.kmzyc.search.facade.constants;

public enum ORDER {
  desc, asc;
  public ORDER reverse() {
    return (this == asc) ? desc : asc;
  }
}
