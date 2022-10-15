package io.github.usbharu.imagesearch.domain.service.scan;

import io.github.usbharu.imagesearch.domain.model.Image;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public interface ScannerLoader {
  List<Scanner> getScanner();
  Image getMetadata(File image, Path subpath);
  Filter getFilter();
  Unifier getUnifier();
}
