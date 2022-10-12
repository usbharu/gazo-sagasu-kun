package io.github.usbharu.imagesearch.util;

import static io.github.usbharu.imagesearch.domain.validation.Validation.require;

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

  private final String jpgOrPng = "(jpg|jpeg|JPG|JPEG|png|PNG)$";

  private final String pixivTypeFileBaseName = "^(\\d+)_p\\d+\\." + jpgOrPng;
  private final String pixivTypeFileNumber = "^\\d+_p(\\d+)\\." + jpgOrPng;
  private final Pattern isPixivTypeFileName;
  private final Pattern getPixivTypeFileBaseName;

  private final Pattern getPixivTypeFileNumber;

  private final String twitterFileUserName = "(\\w+)-\\d+-img\\d+\\." + jpgOrPng;
  private final String twitterFileId = "\\w+-(\\d+)-img\\d+\\." + jpgOrPng;
  private final String twitterFileNumber = "\\w+-\\d+-img(\\d+)\\." + jpgOrPng;

  private final Pattern twitterFileUserNamePattern;
  private final Pattern twitterFileIdPattern;
  private final Pattern twitterFileNumberPattern;

  @Value(value = "${imagesearch.scan.folder:}")
  private String scanFolder = "";

  private final Logger logger = LoggerFactory.getLogger(ImageFileNameUtil.class);

  public ImageFileNameUtil() {
    isPixivTypeFileName = Pattern.compile(pixivTypeFileBaseName);
    getPixivTypeFileBaseName = Pattern.compile(pixivTypeFileBaseName);
    twitterFileUserNamePattern = Pattern.compile(twitterFileUserName);
    twitterFileIdPattern = Pattern.compile(twitterFileId);
    twitterFileNumberPattern = Pattern.compile(twitterFileNumber);
    getPixivTypeFileNumber = Pattern.compile(pixivTypeFileNumber);
  }

  public boolean isJpg(String name) {
    require().nonNullAndNonBlank(name, "Name is null or blank");
    return name.toUpperCase().endsWith(".JPG") || name.toUpperCase().endsWith(".JPEG");
  }

  public boolean isPng(String name) {
    require().nonNullAndNonBlank(name, "Name is null or blank");
    return name.toUpperCase().endsWith(".PNG");
  }

  public boolean isPixivTypeFileName(String name) {
    require().nonNullAndNonBlank(name, "Name is null or blank");
    return isPixivTypeFileName.matcher(name).find();
  }

  public String getPixivTypeFileBaseName(String name) {
    require().nonNullAndNonBlank(name, "Name is null or blank");
    logger.trace("get pixiv type file base name {}", name);
    final Matcher matcher = getPixivTypeFileBaseName.matcher(name);
    if (matcher.find()) {
      return matcher.group(1);
    }
    return null;
  }

  public String getPixivFileNumber(String name) {
    require().nonNullAndNonBlank(name, "Name is null or blank");
    logger.trace("get pixiv type file number {}", name);
    final Matcher matcher = getPixivTypeFileNumber.matcher(name);
    if (matcher.find()) {
      return matcher.group(1);
    }
    return null;
  }

  public String getTwitterFileUserName(String name) {
    require().nonNullAndNonBlank(name, "Name is null or blank");
    logger.trace("get twitter type file user name {}", name);
    final Matcher matcher = twitterFileUserNamePattern.matcher(name);
    if (matcher.find()) {
      return matcher.group(1);
    }
    return null;
  }

  public String getTwitterFileId(String name) {
    require().nonNullAndNonBlank(name, "Name is null or blank");
    logger.trace("get twitter type file id {}", name);
    Matcher matcher = twitterFileIdPattern.matcher(name);
    if (matcher.find()) {
      return matcher.group(1);
    }
    return null;
  }

  public String getTwitterFileNumber(String name) {
    require().nonNullAndNonBlank(name, "Name is Null or Blank");
    logger.trace("get twitter type file number {}", name);
    Matcher matcher = twitterFileNumberPattern.matcher(name);
    if (matcher.find()) {
      return matcher.group(1);
    }
    return null;
  }

  public boolean isTwitterTypeFileName(String name) {
    require().nonNullAndNonBlank(name, "Name is Null or Blank");
    return twitterFileUserNamePattern.matcher(name).find();
  }

  public String getTwitterUrl(String name) {
    return "https://twitter.com/" + getTwitterFileUserName(name) + "/status/" + getTwitterFileId(
        name) + "/photo/" + getTwitterFileNumber(name);
  }

  public String getPixivUrl(String name) {
    return "https://www.pixiv.net/artworks/" + getPixivTypeFileBaseName(name);
  }

  public String getFullPath(String path) {
    Objects.requireNonNull(path);
    return scanFolder + File.separator + path;
  }

}
