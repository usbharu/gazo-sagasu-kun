package io.github.usbharu.imagesearch.domain.repository;

public enum ImageTagDaoOrder {
  ASC("ASC"), DESC("DESC");

  private final String sql;

  ImageTagDaoOrder(String sql) {
    this.sql = sql;
  }

  public String getSql() {
    return sql;
  }

  public static ImageTagDaoOrder fromString(String value) {
    for (ImageTagDaoOrder order : values()) {
      if (order.getSql().equalsIgnoreCase(value)) {
        return order;
      }
    }
    return ASC;
  }
}
