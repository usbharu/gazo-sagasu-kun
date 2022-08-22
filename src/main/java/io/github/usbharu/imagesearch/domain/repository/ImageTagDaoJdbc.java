package io.github.usbharu.imagesearch.domain.repository;

import io.github.usbharu.imagesearch.domain.model.Group;
import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.model.ImageTag;
import io.github.usbharu.imagesearch.domain.model.Tag;
import io.github.usbharu.imagesearch.domain.model.Tags;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Deprecated
@Repository("ImageTagDaoJdbc")
public class ImageTagDaoJdbc implements ImageTagDao {

  Log log = LogFactory.getLog(ImageTagDaoJdbc.class);

  @Autowired
  private JdbcTemplate jdbcTemplate;


  @Override
  public List<ImageTag> findAll(ImageTagDaoOrderType orderType, ImageTagDaoOrder order) {
    String sql =
        "SELECT image_id,\n"
            + "       image.name             as image_name,\n"
            + "       image.path             as image_url,\n"
            + "       image.groupId      as image_group,\n"
            + "       GROUP_CONCAT(tag_id)   as tags_id,\n"
            + "       GROUP_CONCAT(tag.name) as tags_name\n"
            + "FROM image_tag\n"
            + "         LEFT JOIN tag ON tag.id = image_tag.tag_id\n"
            + "         LEFT JOIN image ON image.id = image_tag.image_id\n"
            + "GROUP BY image_id\n"
            + "ORDER BY " +
            formatOrderSql(orderType, order);
    return parseImageTags(
        jdbcTemplate.queryForList(sql));
  }

  @Override
  public ImageTag findByImageUrl(String url) {
    String sql = "SELECT image_id,\n"
        + "       image.name             as image_name,\n"
        + "       image.path             as image_url,\n"
        + "       image.groupId          as image_group,\n"
        + "       GROUP_CONCAT(tag_id)   as tags_id,\n"
        + "       GROUP_CONCAT(tag.name) as tags_name\n"
        + "FROM image_tag\n"
        + "         LEFT JOIN tag on tag.id = image_tag.tag_id\n"
        + "         LEFT JOIN image on image.id = image_tag.image_id\n"
        + "WHERE image.path = ?\n"
        + "GROUP BY image_id";
    return parseImageTags(
        jdbcTemplate.queryForList(sql, url)).get(0);
  }

  @Override
  public ImageTag findByImageId(int id) {
    String sql = "SELECT image_id,\n"
        + "       image.name             as image_name,\n"
        + "       image.path              as image_url,\n"
        + "       image.groupId           as image_group,\n"
        + "       GROUP_CONCAT(tag_id)   as tags_id,\n"
        + "       GROUP_CONCAT(tag.name) as tags_name\n"
        + "FROM image_Tag\n"
        + "         LEFT JOIN tag on tag.id = image_tag.tag_id\n"
        + "         LEFT JOIN image on image.id = image_tag.image_id\n"
        + "WHERE image.id = ?\n"
        + "GROUP BY image_id";
    return parseImageTags(jdbcTemplate.queryForList(sql, id)).get(0);

  }

  @Override
  public List<ImageTag> findByImageName(String name, ImageTagDaoOrderType orderType,
      ImageTagDaoOrder order) {
    String sql = "SELECT image_id,\n"
        + "       image.name             as image_name,\n"
        + "       image.path             as image_url,\n"
        + "       GROUP_CONCAT(tag_id)   as tags_id,\n"
        + "       GROUP_CONCAT(tag.name) as tags_name\n"
        + "FROM image_tag\n"
        + "         LEFT JOIN tag on tag.id = image_tag.tag_id\n"
        + "         LEFT JOIN image on image.id = image_tag.image_id\n"
        + "WHERE image.name = ?\n"
        + "GROUP BY image_id\n"
        + "ORDER BY " + formatOrderSql(orderType, order);
    return parseImageTags(
        jdbcTemplate.queryForList(sql, name));
  }

  @Override
  public List<ImageTag> findByTagId(int id, ImageTagDaoOrderType orderType,
      ImageTagDaoOrder order) {
    String sql =
        "SELECT image_id,\n"
            + "       image.name             as image_name,\n"
            + "       image.path              as image_url,\n"
            + "       image.groupId          as image_group,\n"
            + "       GROUP_CONCAT(tag_id)   as tags_id,\n"
            + "       GROUP_CONCAT(tag.name) as tags_name\n"
            + "FROM main.image_tag\n"
            + "         LEFT JOIN tag ON tag.id = image_tag.tag_id\n"
            + "         LEFT JOIN image ON image.id = image_tag.image_id\n"
            + "WHERE image_id IN (SELECT image_id FROM image_tag WHERE tag_id = ?)\n"
            + "GROUP BY image_id\n"
            + "ORDER BY " +
            formatOrderSql(orderType, order);
    return parseImageTags(
        jdbcTemplate.queryForList(sql, id));
  }

  @Override
  public List<ImageTag> findByTagName(String name, ImageTagDaoOrderType orderType,
      ImageTagDaoOrder order) {
    String sql =
        "SELECT image_id,\n"
            + "       image.name as image_name,\n"
            + "       image.path as image_url,\n"
            + "       image.groupId as image_group,\n"
            + "       GROUP_CONCAT(tag_id)   as tags_id,\n"
            + "       GROUP_CONCAT(tag.name) as tags_name\n"
            + "FROM image_tag\n"
            + "         LEFT JOIN tag ON tag.id = image_tag.tag_id\n"
            + "         LEFT JOIN image ON image.id = image_tag.image_id\n"
            + "WHERE image_id IN\n"
            + "      (SELECT image_id FROM image_tag WHERE tag_id = (SELECT id FROM tag WHERE name = ?))\n"
            + "GROUP BY image_id\n"
            + "ORDER BY " + formatOrderSql(orderType, order);
    return parseImageTags(
        jdbcTemplate.queryForList(sql, name));
  }

  @Override
  public List<ImageTag> findByTagIds(List<Integer> ids, ImageTagDaoOrderType orderType,
      ImageTagDaoOrder order) {
    String sql =
        "SELECT image_id,\n"
            + "       image.name             as image_name,\n"
            + "       image.path             as image_url,\n"
            + "       image.groupId          as image_group,\n"
            + "       GROUP_CONCAT(tag_id)   as tags_id,\n"
            + "       GROUP_CONCAT(tag.name) as tags_name\n"
            + "FROM image_tag\n"
            + "         JOIN image ON image_tag.image_id = image.id\n"
            +
            "         JOIN tag ON tag.id = image_tag.tag_id\n"
            + "\n"
            + "WHERE image_id IN (SELECT image.id\n"
            + "                   FROM image\n"
            + "                            JOIN image_tag ON image.id = image_tag.image_id\n"
            + "                            JOIN tag ON image_tag.tag_id = tag.id\n"
            + "                   WHERE tag.id IN (" + integerFormat(ids) + ")\n"
            + "                   GROUP BY image.id\n"
            + "                   HAVING COUNT(image.id) >= ?)\n"
            + "GROUP BY image_id\n"
            + "ORDER BY " +
            formatOrderSql(orderType, order);
    return parseImageTags(
        jdbcTemplate.queryForList(sql, ids.size()));
  }

  @Deprecated
  @Override
  public List<ImageTag> findByTagNames(List<String> names, ImageTagDaoOrderType orderType,
      ImageTagDaoOrder order) {
    String sql =
        "SELECT image_id,\n"
            + "       image.name as image_name,\n"
            + "       image.path as image_url,\n"
            + "       image.groupId as image_group,\n"
            + "       GROUP_CONCAT(tag_id) as tags_id,\n"
            + "       GROUP_CONCAT(tag.name) as tags_name\n"
            + "FROM image_tag\n"
            + "         JOIN main.image on image.id = image_tag.image_id\n"
            + "         JOIN tag ON tag.id = image_tag.tag_id\n"
            + "WHERE image_tag.image_id IN (SELECT image.id\n"
            + "                   FROM image\n"
            + "                            JOIN image_tag ON image.id = image_tag.image_id\n"
            + "                            JOIN tag ON image_tag.tag_id = tag.id\n"
            + "                   WHERE tag.name IN (" + stringFormat(names) + ")\n"
            + "                   GROUP BY image.id\n"
            + "                   HAVING COUNT(image.id) >= ?)\n"
            + "GROUP BY image_id \n"
            + "ORDER BY " + formatOrderSql(orderType, order);
    return parseImageTags(
        jdbcTemplate.queryForList(sql, names.size()));
  }

  public List<Image> findByTagNames2(List<String> tags, ImageTagDaoOrderType orderType,
      ImageTagDaoOrder order) {
    String sql =
        "SELECT image_id,\n"
            + "       image.name as image_name,\n"
            + "       image.path as image_url,\n"
            + "       image.groupId as image_group,\n"
            + "       groupId.name as group_name,\n"
            + "       GROUP_CONCAT(tag_id) as tags_id,\n"
            + "       GROUP_CONCAT(tag.name) as tags_name\n"
            + "FROM image_tag\n"
            + "         JOIN image on image.id = image_tag.image_id\n"
            + "         JOIN tag ON tag.id = image_tag.tag_id\n"
            + "         JOIN groupID ON groupId = groupId.id\n"
            + "WHERE image_id IN (SELECT image.id\n"
            + "                   FROM image\n"
            + "                            JOIN image_tag ON image.id = image_tag.image_id\n"
            + "                            JOIN tag ON image_tag.tag_id = tag.id\n"
            + "                   WHERE tag.name IN (" + stringFormat(tags) + ")\n"
            + "                   GROUP BY image.id\n"
            + "                   HAVING COUNT(image.id) >= ?)\n"
            + "GROUP BY image_id\n"
            + "ORDER BY " + formatOrderSql(orderType, order);
    return parseImages(jdbcTemplate.queryForList(sql, tags.size()));
  }

  private List<Image> parseImages(List<Map<String, Object>> queryForList) {
    List<Image> result = new ArrayList<>();
    for (Map<String, Object> stringObjectMap : queryForList) {
      Image image = new Image((Integer) stringObjectMap.get("image_id"),
          (String) stringObjectMap.get("image_name"), (String) stringObjectMap.get("image_url"));
      Tags tags = new Tags();
      tags.addAll(parseTag(stringObjectMap));
      image.getMetadata().add(new Group((Integer) stringObjectMap.get("image_group"),
          (String) stringObjectMap.get("group_name")));
      image.getMetadata().add(tags);
      result.add(image);
    }
    return result;
  }

  public int insertOne(ImageTag imageTag) {
    Image image = imageTag.getImage();
    int count = 0;
    for (Tag tag : imageTag.getTags()) {
      log.debug("Inserting image tag: {}" + imageTag);
      count += jdbcTemplate.update(
          "INSERT INTO image_tag(image_id,tag_id) SELECT ?,? WHERE NOT EXISTS(SELECT 1 FROM image_tag WHERE image_id = ? AND tag_id = ?)",
          image.getId(), tag.getId(), image.getId(), tag.getId());
    }
    return count;
  }

  public int deleteOne(ImageTag imageTag) {
    Image image = imageTag.getImage();
    int count = 0;
    for (Tag tag : imageTag.getTags()) {
      count += jdbcTemplate.update("DELETE FROM image_tag WHERE image_id = ? AND tag_id = ?",
          image.getId(), tag.getId());
    }
    return count;
  }

  public int deleteByTagId(int id) {
    return jdbcTemplate.update("DELETE FROM main.image_tag WHERE tag_id = ?", id);
  }

  private List<ImageTag> parseImageTags(List<Map<String, Object>> mapList) {
    List<ImageTag> result = new ArrayList<>();

    for (Map<String, Object> stringObjectMap : mapList) {
      result.add(new ImageTag(new Image((Integer) stringObjectMap.get("image_id"),
          (String) stringObjectMap.get("image_name"), (String) stringObjectMap.get("image_url"),
          (Integer) stringObjectMap.get("image_group")),
          parseTag(stringObjectMap)));
    }
    return result;
  }

  private List<Tag> parseTag(Map<String, Object> map) {
    String[] tagsId = ((String) map.get("tags_id")).split(",");
    String[] tagsName = ((String) map.get("tags_name")).split(",");
    List<Tag> result = new ArrayList<>();
    for (int i = 0; i < tagsId.length; i++) {
      result.add(new Tag(Integer.parseInt(tagsId[i]), tagsName[i]));
    }
    return result;
  }

  private String stringFormat(List<String> names) {
    if (names.isEmpty()) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    sb.append("'").append(names.get(0)).append("'");
    for (int i = 1, namesSize = names.size(); i < namesSize; i++) {
      String name = names.get(i);
      sb.append(",").append("'").append(name).append("'");
    }
    return sb.toString();
  }

  private String integerFormat(List<Integer> ids) {
    if (ids.isEmpty()) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    sb.append(ids.get(0));
    for (int i = 1, idsSize = ids.size(); i < idsSize; i++) {
      Integer id = ids.get(i);
      sb.append(",").append(id);
    }
    return sb.toString();
  }

  private String formatOrderSql(ImageTagDaoOrderType type, ImageTagDaoOrder order) {
    String sql = type.getSql();
    if (type == ImageTagDaoOrderType.RANDOM) {
      return sql;
    }
    return sql + " " + order.getSql();
  }
}
