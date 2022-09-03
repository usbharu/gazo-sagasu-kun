package io.github.usbharu.imagesearch.pixiv;

import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.model.ImageMetadata;
import io.github.usbharu.imagesearch.domain.model.Tag;
import io.github.usbharu.imagesearch.domain.model.Tags;
import io.github.usbharu.imagesearch.image.DefaultJpegScanner;
import io.github.usbharu.imagesearch.image.Scanner;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "imagesearch.scan.impl", havingValue = "pixiv")
public class PixivBatchDownloaderPluginScanner extends DefaultJpegScanner implements Scanner {

  Logger logger = LoggerFactory.getLogger(PixivBatchDownloaderPluginScanner.class);

  @Override
  public boolean isSupported(File file) {
    return imageFileNameUtil.isJpg(file.getName()) || file.getName().toUpperCase().endsWith("PNG");
  }

  @Override
  public Image getMetadata(File imageFile, Path subpath) {
    Image image = null;
    if (imageFileNameUtil.isJpg(imageFile.getName())) {
      image = super.getMetadata(imageFile, subpath);
    }

    if (image == null) {
      image = new Image(imageFile.getName(), subpath.toString());
    }

    if (imageFileNameUtil.isPixivTypeFileName(imageFile.getName())) {
      image.getMetadata().addAll(getPixivImageMetadata(imageFile));
      Tags metadata = new Tags();
      metadata.add(new Tag("__"+imageFileNameUtil.getPixivTypeFileBaseName(imageFile.getName())+"__"));
      metadata.add(new Tag("__Pixiv__"));
      image.addMetadata(metadata);
      logger.trace(image.toString());
    }
    return image;
  }

  protected List<ImageMetadata> getPixivImageMetadata(File image) {
    File file = new File(
        image.getParent() + File.separator + imageFileNameUtil.getPixivTypeFileBaseName(
            image.getName()) + "-meta.txt");
    if (file.exists()) {
      return PixivMetadataParser.parse(file);
    }
    return List.of();

  }


}
