package io.github.usbharu.imagesearch.domain.model;

public class ImageTag {
  private final Image image;
  private final Tag[] tags;

  public ImageTag(Image image, Tag[] tags) {
    this.image = image;
    this.tags = tags;
  }

  public Image getImage() {
    return image;
  }

  public Tag[] getTags() {
    return tags;
  }
}
