package io.github.usbharu.imagesearch.domain.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Image {

  private final int id;
  private final String name;
  private final List<ImageMetadata> metadata = new ArrayList<>();
  Logger logger = LoggerFactory.getLogger(Image.class);
  private String path;
  private int group;

  public Image(String name, String path) {
    this.name = name;
    this.path = path;
    group = -1;
    id = -1;
  }

  public Image(int id, String name, String path) {
    this.id = id;
    this.name = name;
    this.path = path;
    group = -1;
  }

  public Image(String name, String path, int group) {
    this.name = name;
    this.path = path;
    this.group = group;
    id = -1;
  }

  public Image(int id, String name, String path, int group) {
    this.id = id;
    this.name = name;
    this.path = path;
    this.group = group;
  }

  @SuppressFBWarnings("EI_EXPOSE_REP")
  public List<ImageMetadata> getMetadata() {
    return metadata;
  }

  public void addMetadata(ImageMetadata metadata) {
    if (metadata == null) {
      return;
    }
    for (ImageMetadata metadatum : getMetadata()) {
      if (metadatum.getType().equals(metadata.getType())) {
        boolean b = metadatum.addMetadata(metadata);
        if (!b) {
          logger.debug("Rejected add metadata at {}", this);
        }
        return;
      }
    }
    getMetadata().add(metadata);
  }

  public String getName() {
    return name;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public int getId() {
    return id;
  }

  public int getGroup() {
    return group;
  }

  public void setGroup(int group) {
    this.group = group;
  }

  @Override
  public String toString() {
    return "Image{" + "id=" + id + ", name='" + name + '\'' + ", path='" + path + '\''
        + ", metadata=" + metadata + ", group=" + group + '}';
  }
}
