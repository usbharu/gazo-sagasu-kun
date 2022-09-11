package io.github.usbharu.imagesearch.domain.service.pixiv;

import static org.junit.jupiter.api.Assertions.*;

import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.service.scan.ScannerTest;
import io.github.usbharu.imagesearch.util.ImageFileNameUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.core.io.ClassPathResource;

class PixivBatchDownloaderPluginScannerTest extends ScannerTest {

  // TODO: 2022/09/08 境界値テストができていないのでする

  @Spy
  ImageFileNameUtil imageFileNameUtil;

  @InjectMocks PixivBatchDownloaderPluginScanner pixivBatchDownloaderPluginScanner;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    scanner = pixivBatchDownloaderPluginScanner;
  }

  @Test
  @Override
  protected void isSupported_nullFile_throwNullPointException() {

  }

  @Test
  void isSupported_nullFile_returnFalseWithWarnLog() {
    assertFalse(pixivBatchDownloaderPluginScanner.isSupported(null));
  }

  @Test
  void isSupported_otherSupportedFile_returnTrue() {
    assertTrue(pixivBatchDownloaderPluginScanner.isSupported(new File("/testData/pixiv/123456_p0.png")));
  }

  @Test
@Override
  protected void getMetadata_hasNoMetadataFile_returnNull(){

  }

  @Test
  void getMetadata_hasNoMetadataFile_returnImageWithMetadata() throws IOException {
    Image metadata =
        scanner.getMetadata(new ClassPathResource("/testData/1/7.jpg").getFile(), Paths.get(""));
    assertNotNull(metadata);
  }
}
