package io.github.usbharu.imagesearch.domain.model;

import java.util.List;

public class ImageTag {

  private final Image image;
  private final List<Tag> tags;

  public ImageTag(Image image, List<Tag> tags) {
    this.image = image;
    this.tags = tags;
  }

  public Image getImage() {
    return image;
  }

  public List<Tag> getTags() {
    return tags;
  }
}
