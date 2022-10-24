package io.github.usbharu.imagesearch.domain.service.scan;

import io.github.usbharu.imagesearch.domain.model.ImageMetadata;
import org.springframework.stereotype.Component;

@Component
public interface Filter {

  ImageMetadata filter(ImageMetadata imageMetadata);

}
