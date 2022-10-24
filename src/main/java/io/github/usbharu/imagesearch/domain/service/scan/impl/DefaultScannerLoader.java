package io.github.usbharu.imagesearch.domain.service.scan.impl;

import io.github.usbharu.imagesearch.domain.service.scan.Filter;
import io.github.usbharu.imagesearch.domain.service.scan.Scanner;
import io.github.usbharu.imagesearch.domain.service.scan.ScannerLoader;
import io.github.usbharu.imagesearch.domain.service.scan.Unifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ConditionalOnProperty(value = "imagesearch.scan.impl", havingValue = "default", matchIfMissing = true)
public class DefaultScannerLoader implements ScannerLoader {

  private static final Filter FILTER = new DefaultFilter();
  private static final Unifier UNIFIER = new DefaultUnifier();

  private static final List<Scanner> SCANNERS = new ArrayList<>();
  final DefaultJpegScanner defaultJpegScanner;

  public DefaultScannerLoader(DefaultJpegScanner defaultJpegScanner) {
    Objects.requireNonNull(defaultJpegScanner);
    SCANNERS.add(defaultJpegScanner);
    this.defaultJpegScanner = defaultJpegScanner;
  }

  @Override
  public List<Scanner> getScanner() {
    return SCANNERS;
  }

  @Override
  public Filter getFilter() {
    return FILTER;
  }

  @Override
  public Unifier getUnifier() {
    return UNIFIER;
  }
}
