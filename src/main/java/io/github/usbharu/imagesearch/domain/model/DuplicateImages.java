package io.github.usbharu.imagesearch.domain.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DuplicateImages extends ArrayList<Image>
    implements ImageMetadata {

  @Override
  public String getType() {
    return "duplicate";
  }

  @Override
  public List<String> getValues() {
    return this.stream().map((Image image) -> {
      System.out.println("image = " + image);
      return String.valueOf(image.getId());
    }).collect(Collectors.toList());
  }

  @Override
  public boolean addMetadata(ImageMetadata metadata) {
    if (getType().equals(metadata.getType())) {
      return addAll((Collection<? extends Image>) metadata);
    }
    return false;
  }
}
