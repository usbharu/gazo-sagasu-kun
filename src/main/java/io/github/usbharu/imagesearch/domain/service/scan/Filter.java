package io.github.usbharu.imagesearch.domain.service.scan;

import io.github.usbharu.imagesearch.domain.model.ImageMetadata;

public interface Filter {
  ImageMetadata filter(ImageMetadata imageMetadata);

}
