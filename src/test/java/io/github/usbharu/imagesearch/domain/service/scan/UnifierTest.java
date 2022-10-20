package io.github.usbharu.imagesearch.domain.service.scan;

import static org.junit.jupiter.api.Assertions.*;

import io.github.usbharu.imagesearch.domain.model.ImageMetadata;
import io.github.usbharu.imagesearch.domain.model.Tag;
import io.github.usbharu.imagesearch.domain.model.Tags;
import org.junit.jupiter.api.Test;

public abstract class UnifierTest {
  protected Unifier unifier;

  @Test
  protected void unify_ImageMetadata_unifiedImageMetadata() {
    Tags imageMetadata = new Tags();
    imageMetadata.add(new Tag("test1"));
    imageMetadata.add(new Tag("test2"));
    imageMetadata.add(new Tag("nKs02J"));
    ImageMetadata actual = unifier.unify(imageMetadata);
    assertNotNull(actual);
  }

  @Test
  protected void unify_null_null() {
    ImageMetadata actual = unifier.unify(null);
    assertNull(actual);
  }
}
