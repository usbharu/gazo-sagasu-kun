package io.github.usbharu.imagesearch.domain.model;

public class Tag {

  private final int id;
  private final String name;


  public Tag(String name){
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
}
