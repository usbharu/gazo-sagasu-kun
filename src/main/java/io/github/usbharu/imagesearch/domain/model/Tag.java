package io.github.usbharu.imagesearch.domain.model;

import java.util.List;

public class Tag implements ImageMetadata {

  private final int id;
  private final String name;


  public Tag(String name) {
    this.id = -1;
    this.name = name;
  }

  public Tag(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getTagName() {
    return "#" + name;
  }

  @Override
  public String getType() {
    return "tag";
  }

  @Override
  public List<String> getValues() {
    return List.of(name);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Tag)) {
      return false;
    }

    Tag tag = (Tag) o;

    return getName().equals(tag.getName());
  }

  @Override
  public int hashCode() {
    int result = getId();
    result = 31 * result + (getName() != null ? getName().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Tag{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}
