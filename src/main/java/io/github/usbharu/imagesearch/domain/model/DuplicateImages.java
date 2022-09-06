package io.github.usbharu.imagesearch.domain.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DuplicateImages extends ArrayList<Image>
    implements ImageMetadata {

  @Override
  public String getType() {
    return "Duplicate";
  }

  @Override
  public List<String> getValues() {
    return this.stream().map(Image::getName).collect(Collectors.toList());
  }

  @Override
  public boolean addMetadata(ImageMetadata metadata) {
    if (getType().equals(metadata.getType())) {
      return addAll((Collection<? extends Image>) metadata);
    }
    return false;
  }
}
