package io.github.usbharu.imagesearch.domain.model;

import java.util.List;

public class LinkTo implements ImageMetadata{

  String link="";

  public LinkTo(String link) {
    this.link = link;
  }

  @Override
  public String getType() {
    return "link";
  }

  @Override
  public List<String> getValues() {
    return List.of(link);
  }

  @Override
  public boolean addMetadata(ImageMetadata metadata) {
    return false;
  }

  @Override
  public String toString() {
    return "LinkTo{" +
        "link='" + link + '\'' +
        '}';
  }
}
