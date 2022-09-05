package io.github.usbharu.imagesearch.domain.model;

import java.util.List;

public class Group implements ImageMetadata {

  private final int id;
  private final String name;

  public Group(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public Group(String name) {
    this.id = -1;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  @Override
  public String getType() {
    return "group";
  }

  @Override
  public List<String> getValues() {
    return List.of(name);
  }

  @Override
  public boolean addMetadata(ImageMetadata metadata) {
    return false;
  }

  @Override
  public String toString() {
    return "Group{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}
