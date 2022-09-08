package io.github.usbharu.imagesearch.domain.service.scan;

import static org.junit.jupiter.api.Assertions.*;

import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.model.Tag;
import io.github.usbharu.imagesearch.util.ImageTagUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

public abstract class ScannerTest {

  protected Scanner scanner;

  @Test
  void isSupported_supportedFile_returnTrue() {
    assertTrue(scanner.isSupported(new File("/testData/1/0.jpg")));
  }

  @Test
  void isSupported_unSupportedFile_returnFalse() {
    assertFalse(scanner.isSupported(new File("/testData/1/0.txt")));
  }

  @Test
  protected void isSupported_nullFile_throwNullPointException() {
    assertThrows(NullPointerException.class, () -> scanner.isSupported(null));
  }

  @Test
  void getMetadata_hasMetadataFIle_returnImageWithMetadata() throws IOException {
    Image metadata =
        scanner.getMetadata(new ClassPathResource("/testData/1/0.jpg").getFile(), Paths.get(""));
    assertTrue(ImageTagUtil.getTags(metadata).contains(new Tag(-1, "tag1")));
  }

  @Test
  protected void getMetadata_hasNoMetadataFile_returnNull() throws IOException {
    Image metadata =
        scanner.getMetadata(new ClassPathResource("/testData/1/7.jpg").getFile(), Paths.get(""));
    assertNull(metadata);
  }

}
