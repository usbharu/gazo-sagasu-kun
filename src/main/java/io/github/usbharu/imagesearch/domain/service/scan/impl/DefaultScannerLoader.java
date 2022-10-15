package io.github.usbharu.imagesearch.domain.service.scan.impl;

import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.service.scan.Filter;
import io.github.usbharu.imagesearch.domain.service.scan.Scanner;
import io.github.usbharu.imagesearch.domain.service.scan.ScannerLoader;
import io.github.usbharu.imagesearch.domain.service.scan.Unifier;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DefaultScannerLoader implements ScannerLoader {

  private static final Filter FILTER = new DefaultFilter();
  private static final Unifier UNIFIER = new DefaultUnifier();

  private static final List<Scanner> SCANNERS = new ArrayList<>();

  public DefaultScannerLoader() {
    SCANNERS.add(new DefaultJpegScanner());
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
