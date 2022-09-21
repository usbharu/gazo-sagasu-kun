package io.github.usbharu.imagesearch.domain.service;

import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.model.Tag;
import io.github.usbharu.imagesearch.domain.model.Tags;
import io.github.usbharu.imagesearch.domain.model.custom.Images;
import io.github.usbharu.imagesearch.domain.repository.TagDao;
import io.github.usbharu.imagesearch.domain.repository.custom.DynamicSearchBuilder;
import io.github.usbharu.imagesearch.domain.repository.custom.DynamicSearchDao;
import io.github.usbharu.imagesearch.domain.service.duplicate.DuplicateCheck;
import io.github.usbharu.imagesearch.util.ImageFileNameUtil;
import io.github.usbharu.imagesearch.util.ImageTagUtil;
import io.github.usbharu.imagesearch.util.ListUtils;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageSearch {

  Logger logger = LoggerFactory.getLogger(ImageSearch.class);
  final DynamicSearchDao dynamicSearchDao;
  private final TagDao tagDao;

  final DuplicateCheck duplicateCheck;

  final
  ImageFileNameUtil imageFileNameUtil;

  public ImageSearch(DynamicSearchDao dynamicSearchDao, TagDao tagDao,
      DuplicateCheck duplicateCheck, ImageFileNameUtil imageFileNameUtil) {
    Objects.requireNonNull(dynamicSearchDao, "DynamicSearchDao is Null");
    Objects.requireNonNull(tagDao, "TagDao is Null");
    Objects.requireNonNull(duplicateCheck, "DuplicateCheck is Null");
    this.dynamicSearchDao = dynamicSearchDao;
    this.tagDao = tagDao;
    this.duplicateCheck = duplicateCheck;
    this.imageFileNameUtil = imageFileNameUtil;
  }

  // TODO: 2022/09/08 randomTagの位置がおかしい
  public Tag randomTag() {
    Tag tag = tagDao.selectRandomOne();
    if (tag == null) {
      return new Tag("ERROR");
    }
    return tag;
  }

  public Images search3(String[] split, String group, String sort, String order, int limit,int page,boolean merge) {
    Images search = dynamicSearchDao.search(
        new DynamicSearchBuilder().setTags(split).setGroup(group).setOrder(order).setOrderType(sort)
            .setPage(page)
            .setLimit(limit).createDynamicSearch());
    if (merge) {
      search = mergeSequentialNumbers(search);
    }
    return search;
  }

  public Images mergeSequentialNumbers(Images images){
    Set<String> tagNames = new HashSet<>();
    Images result = new Images(images.getCount());
    for (Image image : images) {
      if (!imageFileNameUtil.isPixivTypeFileName(image.getName())) {
        result.add(image);
      }
      Tags tags = ImageTagUtil.getTags(image);
      Tag tag = ListUtils.getOr(ImageTagUtil.getMatchedTags(tags, "--\\d+--"), 0, null);
      if (tag == null) {
        continue;
      }
      if (tagNames.add(tag.getName())) {
        result.add(image);
      }
    }
    return result;
  }
}
