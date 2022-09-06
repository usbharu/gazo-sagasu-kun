package io.github.usbharu.imagesearch.domain.repository.custom;

public enum ImageTagDaoOrderType {

  TAG_ID("tag_id"), TAG_NAME("tag_name"), IMAGE_ID("image_id"), IMAGE_URL("image_url"),
  IMAGE_NAME("image_name"), TAG_COUNT("COUNT(tag_id)", "tag_count"), RANDOM("random()", "random");

  private final String sql;
  private final String name;

  ImageTagDaoOrderType(String sql) {
    this.name = sql;
    this.sql = sql;
  }

  ImageTagDaoOrderType(String sql, String name) {
    this.sql = sql;
    this.name = name;
  }

  public static ImageTagDaoOrderType fromString(String value) {
    for (ImageTagDaoOrderType order : values()) {
      if (order.getName().equals(value)) {
        return order;
      }
    }
    return IMAGE_ID;
  }

  public String getSql() {
    return sql;
  }

  @Override
  public String toString() {
    return "ImageTagDaoOrderType{" +
        "sql='" + sql + '\'' +
        '}';
  }

  public String getName() {
    return name;
  }
}
