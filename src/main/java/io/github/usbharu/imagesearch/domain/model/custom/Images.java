package io.github.usbharu.imagesearch.domain.model.custom;

import io.github.usbharu.imagesearch.domain.model.Image;
import java.util.ArrayList;

public class Images extends ArrayList<Image> {

  private int count = 0;

  public Images(int count) {
    this.count = count;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }
}
