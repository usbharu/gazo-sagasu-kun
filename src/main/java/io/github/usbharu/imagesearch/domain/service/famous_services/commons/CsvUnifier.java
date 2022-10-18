package io.github.usbharu.imagesearch.domain.service.famous_services.commons;

import io.github.usbharu.imagesearch.domain.model.ImageMetadata;
import io.github.usbharu.imagesearch.domain.service.scan.Unifier;
import org.springframework.stereotype.Component;

@Component
public class CsvUnifier implements Unifier {

  @Override
  public ImageMetadata unify(ImageMetadata imageMetadata) {
    return imageMetadata;
  }
}
