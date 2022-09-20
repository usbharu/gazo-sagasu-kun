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
    List<ImageMetadata> pixivImageMetadata = getPixivImageMetadata(new File(image.getPath()));
    for (ImageMetadata pixivImageMetadatum : pixivImageMetadata) {
      System.out.println("pixivImageMetadatum = " + pixivImageMetadatum);
      if (pixivImageMetadatum instanceof LinkTo) {
        LinkTo pixivImageMetadatum1 = (LinkTo) pixivImageMetadatum;
        System.out.println(
            "pixivImageMetadatum1.getValues() = " + pixivImageMetadatum1.getValues());
        return pixivImageMetadatum1;
      }
    }
    System.out.println("RETURN NULL");
    return null;
  }

  protected List<ImageMetadata> getPixivImageMetadata(File image) {
    String pathname = File.separator + image.getParent() + File.separator
        + imageFileNameUtil.getPixivTypeFileBaseName(
        image.getName()) + "-meta.txt";
    File file = new File(imageFileNameUtil.getFullPath(pathname));
    System.out.println("file = " + file);
    if (file.exists()) {
      return PixivMetadataParser.parse(file);
    }
    return List.of();

  }

}
