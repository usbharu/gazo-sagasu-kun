package io.github.usbharu.imagesearch.domain.repository.custom;

import io.github.usbharu.imagesearch.domain.repository.custom.DynamicSearchDao.DynamicSearch;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 様々なデータをもとに検索する{@link DynamicSearchDao}に使用する{@link DynamicSearch}のBuilderです。
 *
 * @since 0.0.3
 * @author usbharu
 */
public class DynamicSearchBuilder {

  private List<String> tags = new ArrayList<>();
  private String group = "";
  private String order = "ASC";
  private String orderType = "image_id";

  private int id = -1;

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

  /**
   * グループをセットする
   *
   * @param group セットするグループ 文字列がblankもしくはallという文字列が代入されていた場合無視されます。つまりこれは {@code group.isBlank() || group.equals("all")}が {@code true}の場合に無視されるということです。
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
    Objects.requireNonNull(order,"ImageTagDaoOrder is Null");
    this.order = order.getSql();
    return this;
  }

  /**
   * ソート方式をセットする。
   *
   * @param imageTagDaoOrderType セットするソート方式
   * @return ソート方式がセットされた {@code DynamicSearchBuilder}
   */
  public DynamicSearchBuilder setOrderType(ImageTagDaoOrderType imageTagDaoOrderType) {
    Objects.requireNonNull(imageTagDaoOrderType,"ImageTagDaoOrderType is Null");
    orderType = imageTagDaoOrderType.getSql();
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

  /**
   * Builderをビルドします。
   *
   * @return ビルドされた {@code DynamicSearch}
   */
  public DynamicSearch createDynamicSearch() {
    return new DynamicSearch(tags, group, order, orderType, id);
  }
}
