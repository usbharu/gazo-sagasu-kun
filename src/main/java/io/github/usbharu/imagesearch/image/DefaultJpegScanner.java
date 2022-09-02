package io.github.usbharu.imagesearch.image;

import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.model.Tag;
import io.github.usbharu.imagesearch.domain.model.Tags;
import io.github.usbharu.imagesearch.util.ImageFileNameUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.constants.MicrosoftTagConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "imagesearch.scan.impl",havingValue = "default",matchIfMissing = true)
public class DefaultJpegScanner implements Scanner {

  Logger logger = LoggerFactory.getLogger(DefaultJpegScanner.class);

  @Autowired protected ImageFileNameUtil imageFileNameUtil;

  @Override
  public boolean isSupported(File file) {
    return imageFileNameUtil.isJpg(file.getName());
  }

  @Override
  public Image getMetadata(File image, Path subpath) {
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
      logger.warn(e.getMessage(), e);
      logger.warn("image:" + image);
    }
    return null;
  }
}
