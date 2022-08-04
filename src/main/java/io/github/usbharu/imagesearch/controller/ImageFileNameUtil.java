package io.github.usbharu.imagesearch.controller;

import java.io.File;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//@ConfigurationProperties("imagesearch")
@Component
public class ImageFileNameUtil {
  private ImageFileNameUtil(){}



@Value("${imagesearch.httpImageFolder}")
  public void setFilePath(String filePath) {
    ImageFileNameUtil.filePath = filePath;
  }
@Value("${imagesearch.httpImageFolder}")
  public static String filePath;
  public static String getURL(File file) throws UnknownHostException {
    Path path = file.toPath();
    String s = filePath +"\\"+ path.subpath(path.getNameCount() - 2, path.getNameCount());
    return s;
  }
}
