package io.github.usbharu.imagesearch.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import io.github.usbharu.imagesearch.util.ImageTagUtil;
import org.junit.jupiter.api.Test;

class ImageTest {

  @Test
  void addMetadata_NoContains_addMetadata() {
    Image image = new Image("test","/test");
    Tags metadata = new Tags();
    metadata.add(new Tag("test1"));
    metadata.add(new Tag("test2"));
    image.addMetadata(metadata);

    assertEquals(2,ImageTagUtil.getTags(image).size());
  }

  @Test
  void addMetadata_2Tags_margeToFirst() {
    Image image = new Image("test", "/test");
    Tags metadata2 = new Tags();
    metadata2.add(new Tag("test1-1"));
    metadata2.add(new Tag("test1-2"));
    Tags metadata = new Tags();
    metadata.add(new Tag("test2-1"));
    metadata.add(new Tag("test2-2"));
    image.addMetadata(metadata2);
    image.addMetadata(metadata);

    System.out.println(image.getMetadata());

    assertEquals(4,ImageTagUtil.getTags(image).size());
  }

}
