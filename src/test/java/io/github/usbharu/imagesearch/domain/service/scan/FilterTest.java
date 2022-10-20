package io.github.usbharu.imagesearch.domain.service.scan;

import static org.junit.jupiter.api.Assertions.*;

import io.github.usbharu.imagesearch.domain.model.Tag;
import io.github.usbharu.imagesearch.domain.model.Tags;
import org.junit.jupiter.api.Test;

public abstract class FilterTest {

  protected Filter filter;

  @Test
  protected void filter_ImageMetadata_returnFilteredImageMetadataOrNull() {
    Tags tags = new Tags();
    tags.add(new Tag("test1"));
    assertDoesNotThrow(() -> filter.filter(tags));
  }

  @Test
  protected void filter_null_returnNull() {
    assertNull(filter.filter(null));
  }
}
