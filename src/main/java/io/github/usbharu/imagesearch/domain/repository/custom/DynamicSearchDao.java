package io.github.usbharu.imagesearch.domain.repository.custom;

import static io.github.usbharu.imagesearch.util.ImageTagUtil.parseImage;

import io.github.usbharu.imagesearch.domain.model.Group;
import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.model.Tag;
import io.github.usbharu.imagesearch.domain.model.Tags;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * 様々な情報から画像を検索するためのDao
 *
 * @author usbharu
 * @since 0.0.3
 */
@Repository
public class DynamicSearchDao {

  private final JdbcTemplate jdbcTemplate;
  Logger logger = LoggerFactory.getLogger(DynamicSearch.class);

  @Autowired
  public DynamicSearchDao(JdbcTemplate jdbcTemplate) {
    Objects.requireNonNull(jdbcTemplate, "JdbcTemplate is Null");
    this.jdbcTemplate = jdbcTemplate;
  }

  /**
   * {@link DynamicSearch}をもとにSQLを発行し、クエリを実行します。
   * 画像データベースから検索をしますが、その他の情報を参照することもあります。
   *
   * @param dynamicSearch 実行する検索の内容
   * @return 検索結果 {@code null} になることはない
   */
// TODO: 2022/09/15 分割するべき
  public List<Image> search(DynamicSearch dynamicSearch) {
    dynamicSearch = Objects.requireNonNullElse(dynamicSearch, new DynamicSearch());
    StringBuilder sb = new StringBuilder();
    if (!dynamicSearch.tags.isEmpty()) {

      sb.append("'");
      for (String tag : dynamicSearch.tags) {
        sb.append(tag).append("','");
      }
      sb.deleteCharAt(sb.length() - 1);
      sb.deleteCharAt(sb.length() - 1);
    }
    String tagsSql = "AND image_id IN (SELECT image.id\n" + "FROM image\n"
        + "JOIN image_tag ON image.id = image_tag.image_id\n"
        + "JOIN tag ON image_tag.tag_id = tag.id\n" + "WHERE tag.name IN (" + sb + ")\n"
        + "GROUP BY image.id\n" + "HAVING COUNT(image.id) >= " + dynamicSearch.tags.size() + ")\n";

    String groupSql = "AND group_name = '" + dynamicSearch.group + "'\n";

    String idSql = "AND image_id = " + dynamicSearch.id + "\n";

    String sql = "SELECT image_id,\n" + "       image.name             as image_name,\n"
        + "       image.path             as image_path,\n"
        + "       image.groupId          as image_group,\n"
        + "       groupId.name           as group_name,\n"
        + "       GROUP_CONCAT(tag_id)   as tags_id,\n"
        + "       GROUP_CONCAT(tag.name) as tags_name\n" + "FROM image_tag\n"
        + "         JOIN image on image.id = image_tag.image_id\n"
        + "         JOIN tag on tag.id = image_tag.tag_id\n"
        + "         JOIN groupId on groupId = groupId.id\n" + "WHERE TRUE\n" + (
        dynamicSearch.tags.isEmpty() ? "\n" : tagsSql) + (
        dynamicSearch.group.isBlank() ? "\n" : groupSql) + (
        dynamicSearch.id <= 1 ? "\n" : idSql) + "GROUP BY image_id\n" + "ORDER BY "
        + dynamicSearch.orderType + " " + dynamicSearch.order;
    List<Image> images = new ArrayList<>();
    try {

      List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);

      for (Map<String, Object> map : maps) {
        Image image = parseImage(map);
        image.getMetadata()
            .add(new Group((Integer) map.get("image_group"), (String) map.get("group_name")));
        Tags tags = new Tags();
        String[] tagIds = ((String) map.get("tags_id")).split(",");
        String[] tagNames = ((String) map.get("tags_name")).split(",");
        List<Tag> tagList = new ArrayList<>();
        for (int i = 0; i < tagIds.length; i++) {
          tagList.add(new Tag(Integer.parseInt(tagIds[i]), tagNames[i]));
        }
        tags.addAll(tagList);
        image.getMetadata().add(tags);
        images.add(image);
      }
    } catch (DataAccessException e) {
      logger.warn("Failed Dynamic Search",e);
    }
    return images;
  }

  /**
   * 検索に使用されるデータをまとめるクラス
   *
   * @since 0.0.3
   * @author usbharu
   */
  public static class DynamicSearch {

    private final List<String> tags;
    private final String group;
    private final String order;
    private final String orderType;

    private final int id;

    /**
     * 検索に使用されるデータを初期化します。 専用のBuilder{@link DynamicSearchBuilder}を使ってください。
     *
     * @param tags      検索するタグのリスト
     * @param group     検索するグループ
     * @param order     並べ替え
     * @param orderType 並べ替えのタイプ
     * @param id        画像のid
     */
    public DynamicSearch(List<String> tags, String group, String order, String orderType, int id) {
      Objects.requireNonNull(tags, "Tags is Null");
      Objects.requireNonNull(group, "Group is Null");
      Objects.requireNonNull(order, "Order is Null");
      Objects.requireNonNull(orderType, "OrderType is Null");
      this.tags = tags;
      this.group = group;
      this.order = order;
      this.orderType = orderType;
      this.id = id;
    }

    /**
     * すべて初期値で初期化します。 専用のBuilder {@link DynamicSearchBuilder}を使ってください
     */
    public DynamicSearch() {
      this.tags = new ArrayList<>();
      this.group = "";
      this.order = "";
      this.orderType = "";
      this.id = 1;
    }
  }

}
