package io.github.usbharu.imagesearch.domain.repository;

public enum TagDaoOrderType {
  TAG_ID("tag_id");

  private final String sql;
  private final String name;

  TagDaoOrderType(String sql, String name) {
    this.sql = sql;
    this.name = name;
  }

  TagDaoOrderType(String sql) {
    this.sql = sql;
    this.name = sql;
  }

  public static TagDaoOrderType fromString(String value) {
    for (TagDaoOrderType order : values()) {
      if (order.getName().equals(value)) {
        return order;
      }
    }
    throw new IllegalArgumentException("Invalid value: " + value);
  }

  public String getSql() {
    return sql;
  }

  public String getName() {
    return name;
  }
}
