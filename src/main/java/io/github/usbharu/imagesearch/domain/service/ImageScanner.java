package io.github.usbharu.imagesearch.domain.service;

import io.github.usbharu.imagesearch.domain.model.Group;
import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.model.ImageTag;
import io.github.usbharu.imagesearch.domain.model.Tag;
import io.github.usbharu.imagesearch.domain.repository.BulkInsertDao;
import io.github.usbharu.imagesearch.domain.repository.GroupDao;
import io.github.usbharu.imagesearch.util.ImageFileNameUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.constants.MicrosoftTagConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@ConfigurationProperties("imagesearch.scan")
@Service
public class ImageScanner {


  private final GroupDao groupDao;
  private final Map<String, List<Path>> pathsMap = new HashMap<>();
  private final List<ImageTag> imageTags = new ArrayList<>();
  private final BulkInsertDao bulkInsertDao;
  Log log = LogFactory.getLog(ImageScanner.class);
  private Map<String, String> group;
  private int depth = 3;
  private String folder = "";

  @Autowired
  public ImageScanner(BulkInsertDao bulkInsertDao, GroupDao groupDao) {
    this.bulkInsertDao = bulkInsertDao;
    this.groupDao = groupDao;
  }

  public void startScan() {
    File file = Paths.get(folder).toFile();
    for (Entry<String, String> stringStringEntry : group.entrySet()) {
      String value = stringStringEntry.getValue();
      List<Path> paths = new ArrayList<>();
      for (String s : value.split("[;:]]")) {
        if (s.isBlank()) {
          continue;
        }
        paths.add(Paths.get(s));
      }
      pathsMap.put(stringStringEntry.getKey(), paths);
    }
    if (!file.isDirectory()) {
      log.warn(folder + " is not directory");
    }
    imageTags.clear();
    scanFolder(file, 0);
    log.info("endScan");
    log.debug("map:" + pathsMap);
    log.debug("imageTags:" + imageTags.size());
    bulkInsertDao.insert(imageTags);
  }

  private void scanFolder(File file, int depth) {
    log.debug("depth:" + depth + " , file:" + file);
    if (depth >= this.depth) {
      return;
    }
    for (File listFile : file.listFiles()) {
      if (listFile.isDirectory()) {
        scanFolder(listFile, depth + 1);
      } else if (ImageFileNameUtil.isJpg(listFile.getName())) {
        scanImage(listFile);
      }
    }
  }

  protected void scanImage(File image) {
    for (Entry<String, List<Path>> stringListEntry : pathsMap.entrySet()) {
      for (Path path : stringListEntry.getValue()) {
        Path imagePath = image.toPath();
        if (imagePath.startsWith(path)) {
          Path subpath = imagePath.subpath(path.getNameCount() - 1, imagePath.getNameCount());
          ImageTag metadata = getMetadata(image, subpath);
          Group group1 = groupDao.insertOneWithReturnGroup(stringListEntry.getKey());
          if (metadata != null) {
            imageTags.add(new ImageTag(
                new Image(metadata.getImage().getName(), metadata.getImage().getPath(),
                    group1.getId()), metadata.getTags()));
          }
          return;
        }
      }
    }
  }

  protected ImageTag getMetadata(File image, Path subpath) {
    try {
      ImageMetadata metadata = Imaging.getMetadata(image);
      if (metadata == null) {
        log.trace("not metadata");
        return null;
      }
      if (!(metadata instanceof JpegImageMetadata)) {
        log.trace("not jpeg metadata");
        return null;
      }
      JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
      TiffField keywords =
          jpegMetadata.findEXIFValueWithExactMatch(MicrosoftTagConstants.EXIF_TAG_XPKEYWORDS);
      if (keywords == null) {
        log.trace("no keywords");
        return null;
      }

      String[] tags = keywords.getValue().toString().split("[; ,]");
      List<Tag> tagList = new ArrayList<>();
      for (String tag : tags) {
        tagList.add(new Tag(tag));
      }
      Image image1 = new Image(image.getName(), subpath.toString());
      return new ImageTag(image1, tagList);
    } catch (ImageReadException | IOException | IllegalArgumentException e) {
      log.warn(e);
      log.warn("image:" + image);
    }
    return null;
  }

  public int getDepth() {
    return depth;
  }

  public void setDepth(int depth) {
    this.depth = depth;
  }

  public Map<String, String> getGroup() {
    return group;
  }

  public void setGroup(Map<String, String> group) {
    this.group = group;
  }

  public String getFolder() {
    return folder;
  }

  public void setFolder(String folder) {
    this.folder = folder;
  }
}
