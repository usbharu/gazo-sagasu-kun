package io.github.usbharu.imagesearch.domain.service;

import io.github.usbharu.imagesearch.domain.model.Group;
import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.model.Tag;
import io.github.usbharu.imagesearch.domain.model.Tags;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@ConfigurationProperties("imagesearch.scan")
@Service
public class ImageScanner {


  private final GroupDao groupDao;
  private final Map<String, List<Path>> pathsMap = new HashMap<>();
  private final List<Image> images = new ArrayList<>();
  private final BulkInsertDao bulkInsertDao;
  Logger logger = LoggerFactory.getLogger(ImageScanner.class);
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
      logger.warn("{} is not directory",folder);
    }
    images.clear();
    scanFolder(file, 0);
    logger.info("endScan");
    logger.debug("{} pathsMap",pathsMap);
    bulkInsertDao.insert(images);
//    bulkInsertDao.insert(imageTags);
  }

  private void scanFolder(File file, int depth) {
    logger.debug("depth: {} , file: {}",depth,file);
    GroupPathSet group1 = getGroup(file);
    logger.debug("group: {}",group1);
    if (depth >= this.depth) {
      return;
    }
    // TODO: 2022/08/22 ここでグループの判定したほうがループがすくなると思う
    for (File listFile : file.listFiles()) {
      if (listFile.isDirectory()) {
        scanFolder(listFile, depth + 1);
      } else if (ImageFileNameUtil.isJpg(listFile.getName())) {
        scanImage(listFile, group1);
      }
    }
  }

  private GroupPathSet getGroup(File file) {
    for (Entry<String, List<Path>> paths : pathsMap.entrySet()) {
      for (Path path : paths.getValue()) {
        if (file.toPath().startsWith(path)) {
          return new GroupPathSet(groupDao.insertOneWithReturnGroup(paths.getKey()), path);

        }
      }
    }
    return new GroupPathSet(new Group("default"), file.toPath());
  }

  protected void scanImage(File image, GroupPathSet group) {
    Path imagePath = image.toPath();
    Path subpath = imagePath.subpath(group.getPath().getNameCount() - 1, imagePath.getNameCount());

    Image imageObject = getMetadata2(image, subpath);
    if (imageObject == null) {
      return;
    }
    imageObject.getMetadata().add(group.getGroup());
    imageObject.setGroup(group.getGroup().getId());
    images.add(imageObject);

  }

  protected Image getMetadata2(File image, Path subpath) {
    try {
      ImageMetadata metadata = Imaging.getMetadata(image);
      if (metadata == null) {
        return null;
      }
      if (!(metadata instanceof JpegImageMetadata)) {
        logger.trace("not jpeg metadata");
        return null;
      }
      JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
      TiffField keywords =
          jpegMetadata.findEXIFValueWithExactMatch(MicrosoftTagConstants.EXIF_TAG_XPKEYWORDS);
      if (keywords == null) {
        logger.trace("no keywords");
        return null;
      }
      String[] tags = keywords.getValue().toString().split("[; ,]");
      List<Tag> tagList = new ArrayList<>();
      for (String tag : tags) {
        tagList.add(new Tag(tag));
      }
      Image image1 = new Image(image.getName(), subpath.toString());
      Tags tagObject = new Tags();
      tagObject.addAll(tagList);
      image1.getMetadata().add(tagObject);
      return image1;

    } catch (ImageReadException | IOException | IllegalArgumentException e) {
      logger.warn(e.getMessage(),e);
      logger.warn("image:" + image);
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

  private static class GroupPathSet {

    private final Group group;

    private final Path path;

    public GroupPathSet(Group group, Path path) {
      this.group = group;
      this.path = path;
    }

    public Group getGroup() {
      return group;
    }

    public Path getPath() {
      return path;
    }
  }
}
