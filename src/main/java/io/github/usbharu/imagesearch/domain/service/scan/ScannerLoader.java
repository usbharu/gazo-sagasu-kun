package io.github.usbharu.imagesearch.domain.service.scan;

import io.github.usbharu.imagesearch.domain.model.Image;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public interface ScannerLoader {

  List<Scanner> getScanner();

  default Image getMetadata(File image, Path subpath) {
    for (Scanner scanner : getScanner()) {
      if (scanner.isSupported(image)) {
        return scanner.getMetadata(image, subpath);
      }
    }
    return null;
  }

  default boolean isSupported(File file) {
    for (Scanner scanner : getScanner()) {
      if (scanner.isSupported(file)) {
        return true;
      }
    }
    return false;
  }

  Filter getFilter();

  Unifier getUnifier();
}
