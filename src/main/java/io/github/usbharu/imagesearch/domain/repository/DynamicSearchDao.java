package io.github.usbharu.imagesearch.domain.repository;

import io.github.usbharu.imagesearch.domain.model.Group;
import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.model.Tag;
import io.github.usbharu.imagesearch.domain.model.Tags;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DynamicSearchDao {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public DynamicSearchDao(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<Image> search(DynamicSearch dynamicSearch) {

    StringBuilder sb = new StringBuilder();
    if (!dynamicSearch.tags.isEmpty()) {

      sb.append("'");
      for (String tag : dynamicSearch.tags) {
        sb.append(tag).append("','");
      }
      sb.deleteCharAt(sb.length() - 1);
      sb.deleteCharAt(sb.length() - 1);
    }
    String tagsSql = "AND image_id IN (SELECT image.id\n"
        + "FROM image\n"
        + "JOIN image_tag ON image.id = image_tag.image_id\n"
        + "JOIN tag ON image_tag.tag_id = tag.id\n"
        + "WHERE tag.name IN (" + sb + ")\n"
        + "GROUP BY image.id\n"
        + "HAVING COUNT(image.id) >= " + dynamicSearch.tags.size() + ")\n";

    String groupSql = "AND group_name = '" + dynamicSearch.group + "'\n";

    String sql = "SELECT image_id,\n"
        + "       image.name             as image_name,\n"
        + "       image.path             as image_path,\n"
        + "       image.groupId          as image_group,\n"
        + "       groupId.name           as group_name,\n"
        + "       GROUP_CONCAT(tag_id)   as tags_id,\n"
        + "       GROUP_CONCAT(tag.name) as tags_name\n"
        + "FROM image_tag\n"
        + "         JOIN image on image.id = image_tag.image_id\n"
        + "         JOIN tag on tag.id = image_tag.tag_id\n"
        + "         JOIN groupId on groupId = groupId.id\n"
        + "WHERE TRUE\n"
        + (dynamicSearch.tags.isEmpty() ? "\n" : tagsSql)
        + (dynamicSearch.group == null || dynamicSearch.group.isBlank() ? "\n" : groupSql)
        + "GROUP BY image_id\n"
        + "ORDER BY " + dynamicSearch.orderType + " " + dynamicSearch.order;
    System.out.println("sql = " + sql);
    List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
//    List<Map<String,Object>> maps = new ArrayList<>();
    List<Image> images = new ArrayList<>();
    for (Map<String, Object> map : maps) {
      Image image = new Image((Integer) map.get("image_id"), (String) map.get("image_name"),
          (String) map.get("image_path"), (Integer) map.get("image_group"));
      image.getMetadata()
          .add(new Group((Integer) map.get("image_group"), (String) map.get("group_name")));
      Tags tags = new Tags();
      String[] tags_ids = ((String) map.get("tags_id")).split(",");
      String[] tags_name = ((String) map.get("tags_name")).split(",");
      List<Tag> tagList = new ArrayList<>();
      for (int i = 0; i < tags_ids.length; i++) {
        tagList.add(new Tag(Integer.parseInt(tags_ids[i]), tags_name[i]));
      }
      tags.addAll(tagList);
      image.getMetadata().add(tags);
      images.add(image);
    }
    return images;
  }

  public static class DynamicSearch {

    private final List<String> tags;
    private final String group;
    private final String order;
    private final String orderType;

    public DynamicSearch(List<String> tags, String group, String order, String orderType) {
      this.tags = tags;
      this.group = group;
      this.order = order;
      this.orderType = orderType;
    }
  }

}
