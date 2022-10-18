package io.github.usbharu.imagesearch.domain.service.famous_services.commons;

import io.github.usbharu.imagesearch.domain.service.scan.Filter;
import io.github.usbharu.imagesearch.domain.service.scan.Scanner;
import io.github.usbharu.imagesearch.domain.service.scan.ScannerLoader;
import io.github.usbharu.imagesearch.domain.service.scan.Unifier;
import io.github.usbharu.imagesearch.domain.service.scan.impl.DefaultJpegScanner;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ConditionalOnProperty(value = "imagesearch.scan.impl", havingValue = "pixiv")
public class FamousServicesScannerLoader implements ScannerLoader {


  private static final List<Scanner> SCANNERS = new ArrayList<>();
  final DefaultJpegScanner defaultJpegScanner;


  private final CsvTagFilter csvTagFilter;
  private final CsvUnifier csvUnifier;

  public FamousServicesScannerLoader(
      @Qualifier("defaultJpegScanner") DefaultJpegScanner defaultJpegScanner, CsvTagFilter csvTagFilter,
      CsvUnifier csvUnifier) {
    SCANNERS.add(defaultJpegScanner);
    this.defaultJpegScanner = defaultJpegScanner;
    this.csvTagFilter = csvTagFilter;
    this.csvUnifier = csvUnifier;
  }


  @Override
  public List<Scanner> getScanner() {
    return SCANNERS;
  }

  @Override
  public Filter getFilter() {
    return csvTagFilter;
  }

  @Override
  public Unifier getUnifier() {
    return csvUnifier;
  }
}
