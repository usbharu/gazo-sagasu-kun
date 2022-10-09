package io.github.usbharu.imagesearch.domain.service;

import io.github.usbharu.imagesearch.domain.exceptions.GroupDatabaseEmptyException;
import io.github.usbharu.imagesearch.domain.exceptions.IllegalPropertyValueException;
import io.github.usbharu.imagesearch.domain.model.Group;
import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.repository.GroupDao;
import io.github.usbharu.imagesearch.domain.repository.custom.BulkDao;
import io.github.usbharu.imagesearch.domain.service.scan.Scanner;
import io.github.usbharu.imagesearch.util.FileComparator;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@ConfigurationProperties("imagesearch.scan")
@Service
public class ImageScanner {

  private final FileComparator fileComparator = new FileComparator();

  private final GroupDao groupDao;
  private final Map<String, List<Path>> pathsMap = new HashMap<>();
  private final List<Image> images = new ArrayList<>();
  private final BulkDao bulkDao;
  Logger logger = LoggerFactory.getLogger(ImageScanner.class);
  private Map<String, String> group = new HashMap<>();
  private int depth = 4;
  private String folder = "";

  final
  Scanner scanner;

  @Autowired
  public ImageScanner(BulkDao bulkDao, GroupDao groupDao, Scanner scanner) {
    Objects.requireNonNull(bulkDao, "BulkDao is Null");
    Objects.requireNonNull(groupDao, "GroupDao is Null");
    Objects.requireNonNull(scanner, "Scanner is Null");
    this.bulkDao = bulkDao;
    this.groupDao = groupDao;
    this.scanner = scanner;
  }

  public void startScan() {
    validateProperties();
    File file = Paths.get(getFolder()).toFile();
    if (!file.isDirectory()) {
      logger.warn("{} is not directory", getFolder());
      return;
    }
    logger.info("Scan folder :{} folder from  {} ", getDepth(), file);
    bulkDao.delete();
    for (Entry<String, String> stringStringEntry : getGroup().entrySet()) {
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
    logger.debug("Groups :{}", pathsMap);
    synchronized (images) {
      logger.trace("images clear");
      images.clear();
      scanFolder(file, 0);
      logger.info(" Successful scan");
      bulkDao.insertSplit(images, 500);
    }
  }

  private void validateProperties() {
    Objects.requireNonNull(getFolder(), "imagesearch.scan.folder is Null");
    Objects.requireNonNull(getGroup(), "imagesearch.scan.group is Null");
    if (getDepth() <= 0) {
      throw new IllegalPropertyValueException("imagesearch.scan.depth is Negative or Zero depth :"+getDepth());
    }
  }

  private void scanFolder(File file, int depth) {
    if (depth >= getDepth()) {
      return;
    }
    Objects.requireNonNull(file);
    if (!file.exists()) {
      throw new IllegalArgumentException("File is NotFound",
          new FileNotFoundException(file.toString()));
    }
    if (!file.isDirectory()) {
      throw new IllegalArgumentException("File is not Directory " + file);
    }
    if (file.listFiles() == null) {
      throw new IllegalStateException("File has not sub directory or files "+file);
    }

    GroupPathSet group1 = getGroup(file);
    logger.debug("Current depth: {} , group {} , file: {}", depth, group1, file);
    File[] files = file.listFiles();
    Arrays.sort(files,fileComparator);
    for (File listFile : files) {
      if (listFile.isDirectory()) {
        scanFolder(listFile, depth + 1);
      } else if (scanner.isSupported(listFile)) {
        scanImage(listFile, group1);
      }
    }
  }

  private GroupPathSet getGroup(File file) {
    Objects.requireNonNull(file);
    if (!file.isDirectory()) {
      throw new IllegalArgumentException("File is not Directory "+ file);
    }
    for (Entry<String, List<Path>> paths : pathsMap.entrySet()) {
      for (Path path : paths.getValue()) {
        if (file.toPath().startsWith(path)) {
          try {
            return new GroupPathSet(groupDao.insertOneWithReturnGroup(paths.getKey()), path);
          }catch (GroupDatabaseEmptyException e){
            logger.warn("Database Select result is 0",e);
            logger.warn("INSERTした直後のSELECTなのでデータベースに異常がある可能性が高い");
          }

        }
      }
    }
    return new GroupPathSet(groupDao.insertOneWithReturnGroup("default"), Paths.get(folder));
  }

  protected void scanImage(File image, GroupPathSet group) {
    Objects.requireNonNull(image,"Image is Null");
    Objects.requireNonNull(group,"GroupPathSet is Null");

    if (!image.isFile()) {
      throw new IllegalArgumentException("Image is not File :"+image);
    }
    logger.trace("Scan Image :{}", image);
    Path imagePath = image.toPath();
    Path subpath = imagePath.subpath(Paths.get(folder).getNameCount(), imagePath.getNameCount());
    logger.trace("Group name: {}  count: {} subpath: {} image path: {}",group.getPath(),group.getPath().getNameCount(),subpath,imagePath);

    Image imageObject = scanner.getMetadata(image, subpath);
    if (imageObject == null) {
      imageObject = new Image(image.getName(), subpath.toString());
    }
    imageObject.getMetadata().add(group.getGroup());
    imageObject.setGroup(group.getGroup().getId());
    logger.trace(imageObject.toString());

    logger.trace("Add Images :{}",imageObject);

    images.add(imageObject);
  }

  public int getDepth() {
    return depth;
  }

  public void setDepth(int depth) {
    if (depth <= 0){
      throw new IllegalArgumentException("Depth is Negative or Zero  Depth :"+depth);
    }
    this.depth = depth;
  }

  public Map<String, String> getGroup() {
    return group;
  }

  public void setGroup(Map<String, String> group) {
    Objects.requireNonNull(group,"Group is Null");
    this.group = group;
  }

  public String getFolder() {
    return folder;
  }

  public void setFolder(String folder) {
    Objects.requireNonNull(folder,"Folder is Null");
    this.folder = folder;
  }

  private static class GroupPathSet {

    private final Group group;

    private final Path path;

    public GroupPathSet(Group group, Path path) {
      Objects.requireNonNull(group,"Group is Null");
      Objects.requireNonNull(path,"Path is Null");
      this.group = group;
      this.path = path;
    }

    public Group getGroup() {
      return group;
    }

    public Path getPath() {
      return path;
    }

    @Override
    public String toString() {
      return "GroupPathSet{" +
          "group=" + group +
          ", path=" + path +
          '}';
    }
  }


}
