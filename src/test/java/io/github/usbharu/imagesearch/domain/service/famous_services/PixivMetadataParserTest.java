package io.github.usbharu.imagesearch.domain.service.famous_services;

import static io.github.usbharu.imagesearch.util.ImageTagUtil.getTagsNoNull;
import static org.junit.jupiter.api.Assertions.*;

import io.github.usbharu.imagesearch.domain.model.ImageMetadata;
import io.github.usbharu.imagesearch.domain.service.famous_services.pixiv.PixivMetadataParser;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

class PixivMetadataParserTest {

  @Test
  void parse_parseMetaFile_returnMetadataList() throws IOException {
    List<ImageMetadata> parse = PixivMetadataParser.parse(
        new ClassPathResource("/testData/pixiv/123456-meta.txt").getFile());
    assertEquals(5, getTagsNoNull(parse).size());
  }

  @Test
  void parse_parseNull_throwNullPointException() {
    assertThrows(NullPointerException.class, () -> PixivMetadataParser.parse(null));
  }

  @Test
  void parse_parseNonExistFile_throwUncheckedIOException() {
    assertThrows(RuntimeException.class, () -> PixivMetadataParser.parse(new File("")));
  }

  @Test
  void parse_parseDirectory_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> PixivMetadataParser.parse(new File("/")));
  }
}
