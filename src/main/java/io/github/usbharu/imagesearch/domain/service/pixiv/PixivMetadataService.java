package io.github.usbharu.imagesearch.domain.service.pixiv;

import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.model.ImageMetadata;
import io.github.usbharu.imagesearch.domain.model.LinkTo;
import io.github.usbharu.imagesearch.util.ImageFileNameUtil;
import java.io.File;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PixivMetadataService {

  final
  ImageFileNameUtil imageFileNameUtil;

  public PixivMetadataService(ImageFileNameUtil imageFileNameUtil) {
    this.imageFileNameUtil = imageFileNameUtil;
  }

  public LinkTo getPixivLink(Image image) {
    if (imageFileNameUtil.isPixivTypeFileName(image.getName())) {
      return new LinkTo(imageFileNameUtil.getPixivUrl(image.getName()));
    }
    return null;
  }

  protected List<ImageMetadata> getPixivImageMetadata(File image) {
    String pathname = File.separator + image.getParent() + File.separator
        + imageFileNameUtil.getPixivTypeFileBaseName(
        image.getName()) + "-meta.txt";
    File file = new File(imageFileNameUtil.getFullPath(pathname));
    if (file.exists()) {
      return PixivMetadataParser.parse(file);
    }
    return List.of();

  }

}
