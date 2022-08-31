package io.github.usbharu.imagesearch.domain.service;

import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.model.Tag;
import io.github.usbharu.imagesearch.domain.repository.DynamicSearchBuilder;
import io.github.usbharu.imagesearch.domain.repository.DynamicSearchDao;
import io.github.usbharu.imagesearch.domain.repository.ImageTagDaoOrder;
import io.github.usbharu.imagesearch.domain.repository.ImageTagDaoOrderType;
import io.github.usbharu.imagesearch.domain.repository.TagDao;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ImageSearch {

  Logger logger = LoggerFactory.getLogger(ImageSearch.class);
  final DynamicSearchDao dynamicSearchDao;
  private final TagDao tagDao;

  public ImageSearch(DynamicSearchDao dynamicSearchDao, TagDao tagDao) {
    this.dynamicSearchDao = dynamicSearchDao;
    this.tagDao = tagDao;
  }

  public List<Image> search3(String[] tags, String group, String orderType, String order) {
    return dynamicSearchDao.search(new DynamicSearchBuilder().setTags(List.of(tags)).setGroup(group)
        .setOrderType(ImageTagDaoOrderType.fromString(orderType))
        .setOrder(ImageTagDaoOrder.fromString(order)).createDynamicSearch());
  }

  public Tag randomTag() {
    return tagDao.selectRandomOne();
  }
}
