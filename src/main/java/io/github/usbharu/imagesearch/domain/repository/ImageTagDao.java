package io.github.usbharu.imagesearch.domain.repository;

import io.github.usbharu.imagesearch.domain.model.ImageTag;
import java.util.List;

public interface ImageTagDao {
  List<ImageTag> findAll(ImageTagDaoOrderType orderType, ImageTagDaoOrder order);

  default List<ImageTag> findAll() {
    return findAll(ImageTagDaoOrderType.IMAGE_ID, ImageTagDaoOrder.ASC);
  }

  ImageTag findByImageUrl(String url);

  ImageTag findByImageId(int id);

  default List<ImageTag> findByImageName(String name) {
    return findByImageName(name, ImageTagDaoOrderType.IMAGE_ID, ImageTagDaoOrder.ASC);
  }

  List<ImageTag> findByImageName(String name, ImageTagDaoOrderType orderType, ImageTagDaoOrder order);

  default List<ImageTag> findByTagId(int id) {
    return findByTagId(id, ImageTagDaoOrderType.IMAGE_ID, ImageTagDaoOrder.ASC);
  }

  List<ImageTag> findByTagId(int id, ImageTagDaoOrderType orderType, ImageTagDaoOrder order);

  default List<ImageTag> findByTagName(String name){
    return findByTagName(name, ImageTagDaoOrderType.IMAGE_ID, ImageTagDaoOrder.ASC);
  }

  List<ImageTag> findByTagName(String name, ImageTagDaoOrderType orderType, ImageTagDaoOrder order);

  default List<ImageTag> findByTagIds(List<Integer> ids) {
    return findByTagIds(ids, ImageTagDaoOrderType.IMAGE_ID, ImageTagDaoOrder.ASC);
  }

  List<ImageTag> findByTagIds(List<Integer> ids, ImageTagDaoOrderType orderType, ImageTagDaoOrder order);

  default List<ImageTag> findByTagNames(List<String> name){
    return findByTagNames(name, ImageTagDaoOrderType.IMAGE_ID, ImageTagDaoOrder.ASC);
  }

  List<ImageTag> findByTagNames(List<String> names, ImageTagDaoOrderType orderType, ImageTagDaoOrder order);

  int insertOne(ImageTag imageTag);
}
