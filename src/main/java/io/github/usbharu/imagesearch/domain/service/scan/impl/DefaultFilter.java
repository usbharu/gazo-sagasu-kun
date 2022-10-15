package io.github.usbharu.imagesearch.domain.service.scan.impl;

import io.github.usbharu.imagesearch.domain.model.ImageMetadata;
import io.github.usbharu.imagesearch.domain.service.scan.Filter;

public class DefaultFilter implements Filter {

  @Override
  public ImageMetadata filter(ImageMetadata imageMetadata) {
    return imageMetadata;
  }
}
