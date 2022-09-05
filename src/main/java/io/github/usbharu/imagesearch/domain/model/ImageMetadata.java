package io.github.usbharu.imagesearch.domain.model;

import java.util.List;

public interface ImageMetadata {

  default String getType() {
    return "";
  }

  List<String> getValues();

  boolean addMetadata(ImageMetadata metadata);


}
