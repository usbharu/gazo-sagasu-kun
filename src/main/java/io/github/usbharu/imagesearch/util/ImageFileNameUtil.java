package io.github.usbharu.imagesearch.util;

import org.springframework.stereotype.Component;


@Component
public class ImageFileNameUtil {

  private ImageFileNameUtil() {
  }

  public static boolean isJpg(String name) {
    return name.toUpperCase().endsWith("JPG") || name.toUpperCase().endsWith("JPEG");
  }

}
