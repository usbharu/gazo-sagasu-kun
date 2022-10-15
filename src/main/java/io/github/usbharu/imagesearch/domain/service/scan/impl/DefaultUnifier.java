package io.github.usbharu.imagesearch.domain.service.scan.impl;

import io.github.usbharu.imagesearch.domain.model.ImageMetadata;
import io.github.usbharu.imagesearch.domain.service.scan.Unifier;

public class DefaultUnifier implements Unifier {

  @Override
  public ImageMetadata unify(ImageMetadata imageMetadata) {
    return imageMetadata;
  }
}
