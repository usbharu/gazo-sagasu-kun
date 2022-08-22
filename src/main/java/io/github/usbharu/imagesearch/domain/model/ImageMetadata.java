package io.github.usbharu.imagesearch.domain.model;

import java.util.List;

public interface ImageMetadata {

  default String getType() {
    return "";
  }

  default List<String> getValues() {
    return List.of();
  }
}
