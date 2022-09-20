package io.github.usbharu.imagesearch.domain.service.twitter;

import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.model.LinkTo;
import io.github.usbharu.imagesearch.util.ImageFileNameUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TwitterMetadataService {
  final
  ImageFileNameUtil imageFileNameUtil;

  public TwitterMetadataService(ImageFileNameUtil imageFileNameUtil) {
    this.imageFileNameUtil = imageFileNameUtil;
  }

  public LinkTo getTwitterUrl(Image image){
    String name = image.getName();
    if (imageFileNameUtil.isTwitterTypeFileName(name)) {
      return new LinkTo(imageFileNameUtil.getTwitterUrl(name));
    }
    return null;
  }
}
