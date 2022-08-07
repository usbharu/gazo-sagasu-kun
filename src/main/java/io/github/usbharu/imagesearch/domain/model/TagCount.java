package io.github.usbharu.imagesearch.domain.model;

public class TagCount {

  private final int count;

  private final Tag tag;

  public TagCount(int count, Tag tag) {
    this.count = count;
    this.tag = tag;
  }

  public int getCount() {
    return count;
  }

  public Tag getTag() {
    return tag;
  }


}
