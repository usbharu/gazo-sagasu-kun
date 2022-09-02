package io.github.usbharu.imagesearch.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Tags extends ArrayList<Tag> implements ImageMetadata {

  @Override
  public String getType() {
    return "tags";
  }

  @Override
  public List<String> getValues() {
    return this.stream().map(Tag::getName).collect(Collectors.toList());
  }

  @Override
  public String toString() {
    return "Tags{} " + super.toString();
  }
}
