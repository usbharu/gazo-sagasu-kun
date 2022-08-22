package io.github.usbharu.imagesearch.domain.model;

import java.util.ArrayList;
import java.util.List;

public class Image {

  private final int id;
  private final String name;
  private final String path;


  private final List<ImageMetadata> metadata = new ArrayList<>();

  private final int group;


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

  public List<ImageMetadata> getMetadata() {
    return metadata;
  }

  public String getName() {
    return name;
  }

  public String getPath() {
    return path;
  }

  public int getId() {
    return id;
  }

  public int getGroup() {
    return group;
  }
}
