package io.github.usbharu.imagesearch.domain.repository;

import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.model.ImageMetadata;
import io.github.usbharu.imagesearch.domain.model.Tag;
import io.github.usbharu.imagesearch.domain.model.Tags;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BulkInsertDao {

  private final JdbcTemplate jdbcTemplate;

  private final TagDao tagDao;

  private final GroupDao groupDao;

  @Autowired
  public BulkInsertDao(JdbcTemplate jdbcTemplate, TagDao tagDao, GroupDao groupDao) {
    this.jdbcTemplate = jdbcTemplate;
    this.tagDao = tagDao;
    this.groupDao = groupDao;
  }

  private Logger logger = LoggerFactory.getLogger(BulkInsertDao.class);

  public void insert2(List<Image> images) {
    if (images.isEmpty()) {
      return;
    }
    List<Tag> tagList = new ArrayList<>();
    for (Image image : images) {
      Tags tags = getTags(image);

      tagList.addAll(tags);

    }
    StringBuilder tagSql = new StringBuilder();
    tagSql.append("INSERT OR IGNORE INTO tag(name) VALUES");
    for (Tag tag : tagList) {
      tagSql.append("('").append(tag.getName()).append("'),");
    }
    tagSql.deleteCharAt(tagSql.length() - 1);
    jdbcTemplate.update(tagSql.toString());

    StringBuilder imageSql = new StringBuilder();
    imageSql.append("INSERT OR IGNORE INTO image(name,path,groupId) VALUES");

    for (Image image : images) {
      imageSql.append("('").append(image.getName()).append("','").append(image.getPath())
          .append("',").append(image.getGroup()).append("),");
    }
    imageSql.deleteCharAt(imageSql.length() - 1);
    imageSql.append("RETURNING id,name,path,groupId");
    List<Image> updatedImageList = parseImage(jdbcTemplate.queryForList(imageSql.toString()));

    logger.debug("{} images have been updated.",updatedImageList.size());

    StringBuilder imageTagSql = new StringBuilder();
    imageTagSql.append("INSERT OR IGNORE INTO image_tag(image_id,tag_id) VALUES");
    List<Image> imageList = parseImage(jdbcTemplate.queryForList("SELECT id,name,path,groupId FROM main.image"));
    if (imageList.isEmpty()) {
      return;
    }
    for (Image image : images) {
      int id = -1;
      for (Image image1 : imageList) {
        if (image1.getPath().equals(image.getPath())) {
          id = image1.getId();
        }
      }

      List<Tag> tags =  getTags(getTags(image));
      for (Tag tag : tags) {
        imageTagSql.append("(").append(id).append(",").append(tag.getId()).append("),");
      }
    }
    imageTagSql.deleteCharAt(imageTagSql.length()-1);

    jdbcTemplate.update(imageTagSql.toString());

  }


  private List<Tag> parseTag(List<Map<String, Object>> maps) {
    List<Tag> result = new ArrayList<>();
    for (Map<String, Object> map : maps) {
      result.add(new Tag(((Integer) map.get("id")), (String) map.get("name")));
    }
    return result;
  }

  private List<Image> parseImage(List<Map<String, Object>> maps) {
    List<Image> result = new ArrayList<>();
    for (Map<String, Object> map : maps) {
      result.add(
          new Image(((Integer) map.get("id")), (String) map.get("name"), (String) map.get("path"),
              (Integer) map.get("groupId")));
    }
    return result;
  }

  private List<Tag> getTags(List<Tag> tags) {
    StringBuilder sb = new StringBuilder();
    sb.append("SELECT id,name FROM tag WHERE name IN (");
    for (Tag tag : tags) {
      sb.append("'").append(tag.getName()).append("',");
    }
    sb.deleteCharAt(sb.length() - 1);
    sb.append(")");
    return parseTag(jdbcTemplate.queryForList(sb.toString()));
  }

  private Tags getTags(Image image) {
    for (ImageMetadata metadatum : image.getMetadata()) {
      if (metadatum instanceof Tags) {
        return ((Tags) metadatum);
      }
    }
    return new Tags();

  }
}
