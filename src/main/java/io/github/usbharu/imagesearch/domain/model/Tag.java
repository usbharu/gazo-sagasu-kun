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
}
