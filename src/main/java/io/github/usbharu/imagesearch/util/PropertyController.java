package io.github.usbharu.imagesearch.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;


@Deprecated
@RestController
public class PropertyController {
  @Value("${imagesearch.httpImageFolder}") private String httpImageFolder;

  public static String HTTP_IMAGE_FOLDER;

  @Value("${imagesearch.httpImageFolder}")
  public void setHttpImageFolder(String httpImageFolder) {
    PropertyController.HTTP_IMAGE_FOLDER = httpImageFolder;
  }
}
