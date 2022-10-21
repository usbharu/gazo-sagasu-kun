package io.github.usbharu.imagesearch.domain.service.famous_services.commons;

import io.github.usbharu.imagesearch.domain.service.famous_services.pixiv.PixivBatchDownloaderPluginScanner;
import io.github.usbharu.imagesearch.domain.service.scan.Filter;
import io.github.usbharu.imagesearch.domain.service.scan.Scanner;
import io.github.usbharu.imagesearch.domain.service.scan.ScannerLoader;
import io.github.usbharu.imagesearch.domain.service.scan.Unifier;
import io.github.usbharu.imagesearch.domain.service.scan.impl.DefaultJpegScanner;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
  private final CsvTagUnifier csvTagUnifier;
  private final PixivBatchDownloaderPluginScanner pixivBatchDownloaderPluginScanner;


  public FamousServicesScannerLoader(
      @Qualifier("defaultJpegScanner") DefaultJpegScanner defaultJpegScanner, CsvTagFilter csvTagFilter,
      CsvTagUnifier csvTagUnifier,
      PixivBatchDownloaderPluginScanner pixivBatchDownloaderPluginScanner) {
    SCANNERS.add(pixivBatchDownloaderPluginScanner);
    SCANNERS.add(defaultJpegScanner);
    this.defaultJpegScanner = defaultJpegScanner;
    this.csvTagFilter = csvTagFilter;
    this.csvTagUnifier = csvTagUnifier;
    this.pixivBatchDownloaderPluginScanner = pixivBatchDownloaderPluginScanner;
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
    return csvTagUnifier;
  }
}
