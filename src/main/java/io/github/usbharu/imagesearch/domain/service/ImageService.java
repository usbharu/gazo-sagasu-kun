package io.github.usbharu.imagesearch.domain.service;

import io.github.usbharu.imagesearch.domain.model.DuplicateImages;
import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.model.custom.Images;
import io.github.usbharu.imagesearch.domain.repository.custom.DynamicSearchBuilder;
import io.github.usbharu.imagesearch.domain.repository.custom.DynamicSearchDao;
import io.github.usbharu.imagesearch.domain.service.duplicate.DuplicateCheck;
import io.github.usbharu.imagesearch.domain.service.famous_services.pixiv.PixivMetadataService;
import io.github.usbharu.imagesearch.domain.service.famous_services.twitter.TwitterMetadataService;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

  private final DynamicSearchDao dynamicSearchDao;

  Logger logger = LoggerFactory.getLogger(ImageSearch.class);
  final DuplicateCheck duplicateCheck;

  final PixivMetadataService pixivMetadataService;
  final
  TwitterMetadataService twitterMetadataService;

  @Autowired
  public ImageService(DynamicSearchDao dynamicSearchDao, DuplicateCheck duplicateCheck,
      PixivMetadataService pixivMetadataService, TwitterMetadataService twitterMetadataService) {
    Objects.requireNonNull(dynamicSearchDao, "DynamicSearchDao is Null");
    Objects.requireNonNull(duplicateCheck, "DuplicateCheck is Null");

    this.dynamicSearchDao = dynamicSearchDao;
    this.duplicateCheck = duplicateCheck;
    this.pixivMetadataService = pixivMetadataService;
    this.twitterMetadataService = twitterMetadataService;
  }

  public Image findById(int id) {
    Images search =
        dynamicSearchDao.search(new DynamicSearchBuilder().setId(id).createDynamicSearch());

    if (search.isEmpty()) {
      return new Image("not found", "");
    }

    Image image = search.get(0);
    DuplicateImages metadata = new DuplicateImages();
    List<Image> check = duplicateCheck.check(image);
    System.out.println("check = " + check);
    metadata.addAll(check);
    image.addMetadata(pixivMetadataService.getPixivLink(image));
    image.addMetadata(twitterMetadataService.getTwitterUrl(image));
    image.addMetadata(metadata);
    logger.trace("View Image :{}", image);
    return image;
  }
}
