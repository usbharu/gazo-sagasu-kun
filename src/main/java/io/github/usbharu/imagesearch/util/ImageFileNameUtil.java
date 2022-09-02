package io.github.usbharu.imagesearch.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties("imagesearch.util.filename")
public class ImageFileNameUtil {

  private String pixivTypeFileBaseName = "(\\d+)";
  private String pixivTypeFileNumber = "_p\\d+\\.";

  private Pattern isPixivTypeFileName;
  private Pattern getPixivTypeFileBaseName;

  private Logger logger = LoggerFactory.getLogger(ImageFileNameUtil.class);

  public ImageFileNameUtil() {
    isPixivTypeFileName = Pattern.compile(pixivTypeFileBaseName + pixivTypeFileNumber);
    getPixivTypeFileBaseName = Pattern.compile(pixivTypeFileBaseName);
  }

  public boolean isJpg(String name) {
    return name.toUpperCase().endsWith("JPG") || name.toUpperCase().endsWith("JPEG");
  }

  public boolean isPng(String name) {
    return name.toUpperCase().endsWith("PNG");
  }

  public boolean isPixivTypeFileName(String name) {
    return isPixivTypeFileName.matcher(name).find();
  }

  public String getPixivTypeFileBaseName(String name) {
    logger.trace("get pixiv type file base name {}", name);
    final Matcher matcher = getPixivTypeFileBaseName.matcher(name);
    if (matcher.find()) {
      return  matcher.group();
    }
    return null;
  }

}
