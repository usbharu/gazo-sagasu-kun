package io.github.usbharu.imagesearch.util;

import io.github.usbharu.imagesearch.domain.validation.StringValidation;
import java.io.File;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties("imagesearch.util.filename")
public class ImageFileNameUtil {

  private String pixivTypeFileBaseName = "(\\d+)";
  private String pixivTypeFileNumber = "_p\\d+\\.";

  private Pattern isPixivTypeFileName;
  private Pattern getPixivTypeFileBaseName;

  @Value(value = "${imagesearch.scan.folder:}")
  private String scanFolder = "";

  private Logger logger = LoggerFactory.getLogger(ImageFileNameUtil.class);

  public ImageFileNameUtil() {
    isPixivTypeFileName = Pattern.compile(pixivTypeFileBaseName + pixivTypeFileNumber);
    getPixivTypeFileBaseName = Pattern.compile(pixivTypeFileBaseName);
  }

  public boolean isJpg(String name) {
    StringValidation.requireNonNullAndNonBlank(name,"Name is null or blank");
    return name.toUpperCase().endsWith("JPG") || name.toUpperCase().endsWith("JPEG");
  }

  public boolean isPng(String name) {
    StringValidation.requireNonNullAndNonBlank(name,"Name is null or blank");
    return name.toUpperCase().endsWith("PNG");
  }

  public boolean isPixivTypeFileName(String name) {
    StringValidation.requireNonNullAndNonBlank(name,"Name is null or blank");
    return isPixivTypeFileName.matcher(name).find();
  }

  public String getPixivTypeFileBaseName(String name) {
    StringValidation.requireNonNullAndNonBlank(name,"Name is null or blank");
    logger.trace("get pixiv type file base name {}", name);
    final Matcher matcher = getPixivTypeFileBaseName.matcher(name);
    if (matcher.find()) {
      return  matcher.group();
    }
    return null;
  }

  public String getFullPath(String path){
    Objects.requireNonNull(path);
    return scanFolder + File.separator + path;
  }

}
