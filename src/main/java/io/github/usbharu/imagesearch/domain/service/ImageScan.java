package io.github.usbharu.imagesearch.domain.service;

import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.model.ImageTag;
import io.github.usbharu.imagesearch.domain.model.Tag;
import io.github.usbharu.imagesearch.domain.repository.ImageDao;
import io.github.usbharu.imagesearch.domain.repository.ImageTagDaoJdbc;
import io.github.usbharu.imagesearch.domain.repository.TagDao;
import io.github.usbharu.imagesearch.util.ImageFileNameUtil;
import io.github.usbharu.imagesearch.util.TimeOut;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.constants.MicrosoftTagConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Deprecated
public class ImageScan {

  @Autowired
  private ImageDao imageDao;

  @Autowired
  private ImageTagDaoJdbc imageTagDaoJdbc;

  @Autowired
  private TagDao tagDao;

  Log log = LogFactory.getLog(ImageScan.class);

  public void scan(Path path) {
    log.debug("scan : " + path);
    if (path.toFile().isFile()) {
      log.warn("scan : " + path + " is file");
      return;
    }
    File[] files = path.toAbsolutePath().toFile().listFiles((File::isDirectory));
    if (files == null) {
      log.warn("scan : " + path + " is null");
      return;
    }
    log.debug("scan : " + path + " has " + files.length + " directories");
    for (int j = 0, filesLength = files.length; j < filesLength; j++) {
      log.trace("start scan : " + files[j]);
      File file = files[j];
      File[] jpegFiles =
          file.listFiles((File file1) -> file1.getName().toLowerCase().endsWith(".jpg"));
      for (int i = 0, jpegFilesLength = jpegFiles.length; i < jpegFilesLength; i++) {
        File jpegFile = jpegFiles[i];
        try {
          TimeOut.with(() -> scanJpegImage(jpegFile),10000);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
          log.warn("scan : " + jpegFile + " failed", e);
        }
      }
    }
    log.debug("scan : " + path + " end");
  }


  void scanJpegImage(File jpegFile) {
    log.trace("scanJpegImage : " + jpegFile);
    try {

      Image image = imageDao.insertOneWithReturnImage(
          new Image(jpegFile.getName(), ImageFileNameUtil.getURL(jpegFile)));
      log.trace("scanJpegImage : " + jpegFile + " image id : " + image.getId());
      ImageMetadata metadata = Imaging.getMetadata(jpegFile.toPath().toAbsolutePath().toFile());
      if (metadata == null) {
        log.trace("scanJpegImage : " + jpegFile + " metadata is null");
        return;
      }
      log.trace("getMetadata : " + jpegFile);
      if (metadata instanceof JpegImageMetadata) {
        TiffField exifValueWithExactMatch =
            ((JpegImageMetadata) metadata).findEXIFValueWithExactMatch(
                MicrosoftTagConstants.EXIF_TAG_XPKEYWORDS);
        if (exifValueWithExactMatch != null) {
          String[] tags = exifValueWithExactMatch.getValue().toString().split("[; ,]");
          log.trace("getTags : "+ Arrays.toString(tags));
          List<Tag> tagsArray = new ArrayList<>();
          for (int i = 0; i < tags.length; i++) {
            tagsArray.set(i, tagDao.insertOneWithReturnTag(tags[i]));
          }
          log.trace("scanJpegImage : " + jpegFile + " tags : " + tagsArray);
          imageTagDaoJdbc.insertOne(new ImageTag(image, tagsArray));
          log.trace("success : " + jpegFile);
        }
      }
    } catch (ImageReadException | IOException e) {
      log.trace("scanJpegImage : " + jpegFile + " failed", e);
    }
  }
}
