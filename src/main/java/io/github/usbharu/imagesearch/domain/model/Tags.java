package io.github.usbharu.imagesearch.domain.model;

import java.util.ArrayList;
import java.util.Collection;
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
  public boolean addMetadata(ImageMetadata metadata) {
    if (metadata.getType().equals(getType()) && metadata instanceof List) {
      return addAll((Collection<? extends Tag>) metadata);
    }
    return false;
  }

  @Override
  public String toString() {
    return "Tags{} " + super.toString();
  }
}
