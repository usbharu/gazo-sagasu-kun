package io.github.usbharu.imagesearch.domain.repository;

import io.github.usbharu.imagesearch.domain.repository.DynamicSearchDao.DynamicSearch;
import java.util.ArrayList;
import java.util.List;

public class DynamicSearchBuilder {

  private List<String> tags = new ArrayList<>();
  private String group = null;
  private String order = "ASC";
  private String orderType = "image_id";

  public DynamicSearchBuilder setTags(List<String> tags) {
    List<String> list = new ArrayList<>();
    for (String tag : tags) {
      if (!tag.isBlank()) {
        list.add(tag);
      }
    }
    this.tags = list;
    return this;
  }

  public DynamicSearchBuilder setGroup(String group) {
    this.group = group;
    return this;
  }

  public DynamicSearchBuilder setOrder(ImageTagDaoOrder order) {
    this.order = order.getSql();
    return this;
  }

  public DynamicSearchBuilder setOrderType(ImageTagDaoOrderType imageTagDaoOrderType) {
    orderType = imageTagDaoOrderType.getSql();
    return this;
  }

  public DynamicSearch createDynamicSearch() {
    return new DynamicSearch(tags, group, order, orderType);
  }
}
