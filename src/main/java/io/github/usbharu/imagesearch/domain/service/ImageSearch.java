package io.github.usbharu.imagesearch.domain.service;

import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.model.Tag;
import io.github.usbharu.imagesearch.domain.repository.TagDao;
import io.github.usbharu.imagesearch.domain.repository.custom.DynamicSearchBuilder;
import io.github.usbharu.imagesearch.domain.repository.custom.DynamicSearchDao;
import io.github.usbharu.imagesearch.domain.repository.custom.ImageTagDaoOrder;
import io.github.usbharu.imagesearch.domain.repository.custom.ImageTagDaoOrderType;
import io.github.usbharu.imagesearch.domain.service.duplicate.DuplicateCheck;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ImageSearch {

  Logger logger = LoggerFactory.getLogger(ImageSearch.class);
  final DynamicSearchDao dynamicSearchDao;
  private final TagDao tagDao;

  final DuplicateCheck duplicateCheck;

  public ImageSearch(DynamicSearchDao dynamicSearchDao, TagDao tagDao,
      DuplicateCheck duplicateCheck) {
    Objects.requireNonNull(dynamicSearchDao, "DynamicSearchDao is Null");
    Objects.requireNonNull(tagDao, "TagDao is Null");
    Objects.requireNonNull(duplicateCheck, "DuplicateCheck is Null");
    this.dynamicSearchDao = dynamicSearchDao;
    this.tagDao = tagDao;
    this.duplicateCheck = duplicateCheck;
  }

  public List<Image> search3(String[] tags, String group, String orderType, String order) {
    logger.info("Search : tags = {}, group = {}, orderType = {}, order = {}", tags, group, orderType,
        order);
    return dynamicSearchDao.search(new DynamicSearchBuilder().setTags(List.of(tags)).setGroup(group)
        .setOrderType(ImageTagDaoOrderType.fromString(orderType))
        .setOrder(ImageTagDaoOrder.fromString(order)).createDynamicSearch());
  }

  // TODO: 2022/09/08 randomTagの位置がおかしい
  public Tag randomTag() {
    Tag tag = tagDao.selectRandomOne();
    if (tag == null) {
      return new Tag("ERROR");
    }
    return tag;
  }
}
