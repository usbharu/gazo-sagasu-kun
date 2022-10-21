package io.github.usbharu.imagesearch.domain.service.famous_services.pixiv;

import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.model.ImageMetadata;
import io.github.usbharu.imagesearch.domain.model.Tag;
import io.github.usbharu.imagesearch.domain.model.Tags;
import io.github.usbharu.imagesearch.domain.service.scan.impl.DefaultJpegScanner;
import io.github.usbharu.imagesearch.domain.service.scan.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class PixivBatchDownloaderPluginScanner extends DefaultJpegScanner implements Scanner {

  Logger logger = LoggerFactory.getLogger(PixivBatchDownloaderPluginScanner.class);

  @Override
  public boolean isSupported(File file) {
    if (file == null) {
      logger.warn("File is Null");
      return false;
    }
    return imageFileNameUtil.isJpg(file.getName()) || file.getName().toUpperCase().endsWith("PNG");
  }

  @Override
  public Image getMetadata(File imageFile, Path subpath) {
    Objects.requireNonNull(imageFile,"ImageFile is Null");
    Objects.requireNonNull(subpath,"Subpath is Null");
    if (!imageFile.exists()) {
      throw new UncheckedIOException("ImageFile is not found",new FileNotFoundException());
    }
    if (!imageFile.isFile()) {
      throw new IllegalArgumentException("ImageFile is not file");
    }


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
      metadata.add(new Tag("--"+imageFileNameUtil.getPixivTypeFileBaseName(imageFile.getName())+"--"));
      metadata.add(new Tag("--Pixiv--"));
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
