package io.github.usbharu.imagesearch.domain.model;

public class Group {

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
}
