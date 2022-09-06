package io.github.usbharu.imagesearch.domain.repository.custom;

public enum TagDaoOrder {
  ASC("ASC"), DESC("DESC");

  private final String sql;

  TagDaoOrder(String sql) {
    this.sql = sql;
  }

  public static TagDaoOrder fromString(String value) {
    for (TagDaoOrder order : values()) {
      if (order.getSql().equalsIgnoreCase(value)) {
        return order;
      }
    }
    throw new IllegalArgumentException("Invalid value: " + value);
  }

  public String getSql() {
    return sql;
  }
}
