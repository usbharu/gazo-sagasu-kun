package io.github.usbharu.imagesearch.domain.repository.custom;

import static io.github.usbharu.imagesearch.domain.validation.Validation.require;

import io.github.usbharu.imagesearch.domain.repository.custom.DynamicSearchDao.DynamicSearch;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 様々なデータをもとに検索する{@link DynamicSearchDao}に使用する{@link DynamicSearch}のBuilderです。
 *
 * @author usbharu
 * @since 0.0.3
 */
public class DynamicSearchBuilder {

  private List<String> tags = new ArrayList<>();
  private String group = "";
  private String order = "ASC";
  private String orderType = "image_id";

  private int id = -1;

  private int limit;
  private int page;

  /**
   * タグをセットする
   *
   * @param tags セットするタグのリスト
   * @return セットされた {@code DynamicSearchBuilder}
   */
  public DynamicSearchBuilder setTags(List<String> tags) {
    Objects.requireNonNull(tags, "Tags is Null");
    List<String> list = new ArrayList<>();
    for (String tag : tags) {
      if (!tag.isBlank()) {
        list.add(tag);
      }
    }
    this.tags = list;
    return this;
  }

  public DynamicSearchBuilder setTags(String[] tags) {
    Objects.requireNonNull(tags, "Tags is Null");
    List<String> list = new ArrayList<>();
    for (String tag : tags) {
      if (!tag.isBlank()) {
        list.add(tag);
      }
    }
    this.tags = list;
    return this;
  }

  /**
   * グループをセットする
   *
   * @param group セットするグループ 文字列がblankもしくはallという文字列が代入されていた場合無視されます。つまりこれは
   *              {@code group.isBlank() || group.equals("all")}が {@code true}の場合に無視されるということです。
   * @return グループがセットされた {@code DynamicSearchBuilder}
   */
  public DynamicSearchBuilder setGroup(String group) {
    if (group == null || group.isBlank() || group.equals("all")) {
      return this;
    }
    this.group = group;
    return this;
  }

  /**
   * ソート方向をセットする。
   *
   * @param order セットするソート方向
   * @return ソート方向がセットされた {@code DynamicSearchBuilder}
   */
  public DynamicSearchBuilder setOrder(ImageTagDaoOrder order) {
    Objects.requireNonNull(order, "ImageTagDaoOrder is Null");
    this.order = order.getSql();
    return this;
  }

  public DynamicSearchBuilder setOrder(String order){
    require().nonNullAndNonBlank(order,"Order is Null or Blank");
    this.order = ImageTagDaoOrder.fromString(order).getSql();
    return this;
  }

  /**
   * ソート方式をセットする。
   *
   * @param imageTagDaoOrderType セットするソート方式
   * @return ソート方式がセットされた {@code DynamicSearchBuilder}
   */
  public DynamicSearchBuilder setOrderType(ImageTagDaoOrderType imageTagDaoOrderType) {
    Objects.requireNonNull(imageTagDaoOrderType, "ImageTagDaoOrderType is Null");
    orderType = imageTagDaoOrderType.getSql();
    return this;
  }

  public DynamicSearchBuilder setOrderType(String orderType) {
    require().nonNullAndNonBlank(orderType, "OrderType is Null or Blank.");
    this.orderType = ImageTagDaoOrderType.fromString(orderType).getSql();
    return this;
  }

  /**
   * IDをセットする
   *
   * @param id セットするID
   * @return IDがセットされた {@code DynamicSearchBuilder}
   */
  public DynamicSearchBuilder setId(int id) {
    this.id = id;
    return this;
  }

  public DynamicSearchBuilder setLimit(int limit) {
    require().positive(limit, "Limit is negative or zero");
    this.limit = limit;
    return this;
  }

  public DynamicSearchBuilder setPage(int page) {
    require().positive(page,"Page is negative or zero");
    this.page = page;
    return this;
  }

  /**
   * Builderをビルドします。
   *
   * @return ビルドされた {@code DynamicSearch}
   */
  public DynamicSearch createDynamicSearch() {
    return new DynamicSearch(tags, group, order, orderType, id, limit,page);
  }
}
