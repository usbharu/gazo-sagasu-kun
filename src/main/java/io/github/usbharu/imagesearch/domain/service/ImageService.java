package io.github.usbharu.imagesearch.domain.service;

import io.github.usbharu.imagesearch.domain.model.DuplicateImages;
import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.repository.custom.DynamicSearchBuilder;
import io.github.usbharu.imagesearch.domain.repository.custom.DynamicSearchDao;
import io.github.usbharu.imagesearch.domain.service.duplicate.DuplicateCheck;
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

  @Autowired
  public ImageService(DynamicSearchDao dynamicSearchDao, DuplicateCheck duplicateCheck) {
    Objects.requireNonNull(dynamicSearchDao, "DynamicSearchDao is Null");
    Objects.requireNonNull(duplicateCheck, "DuplicateCheck is Null");

    this.dynamicSearchDao = dynamicSearchDao;
    this.duplicateCheck = duplicateCheck;
  }

  public Image findById(int id) {
    List<Image> search =
        dynamicSearchDao.search(new DynamicSearchBuilder().setId(id).createDynamicSearch());

    if (search.isEmpty()) {
      return new Image("not found", "");
    }

    Image image = search.get(0);
    DuplicateImages metadata = new DuplicateImages();
    metadata.addAll(duplicateCheck.check(image));
    image.addMetadata(metadata);
    logger.trace("View Image :{}", image);
    return image;
  }
}
