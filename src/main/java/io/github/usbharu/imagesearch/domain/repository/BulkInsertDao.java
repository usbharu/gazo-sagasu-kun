package io.github.usbharu.imagesearch.domain.repository;

import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.model.ImageTag;
import io.github.usbharu.imagesearch.domain.model.Tag;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BulkInsertDao {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private TagDao tagDao;

  public void insert(List<ImageTag> images) {
    List<Tag> tagList = new ArrayList<>();
    for (ImageTag image : images) {
      tagList.addAll(image.getTags());
    }

    StringBuilder sql1 = new StringBuilder();
    sql1.append("INSERT OR IGNORE INTO tag (name) VALUES");
    for (Tag tag : tagList) {
      sql1.append("('").append(tag.getName()).append("'),");
    }
    sql1.deleteCharAt(sql1.length() - 1);
    jdbcTemplate.update(sql1.toString());

    StringBuilder sql2 = new StringBuilder();
    sql2.append("INSERT OR IGNORE INTO image(name,path,groupId) VALUES");
    for (ImageTag image : images) {
      Image image1 = image.getImage();
      sql2.append("('").append(image1.getName()).append("','").append(image1.getPath()).append("',")
          .append(image1.getGroup()).append("),");
    }
    sql2.deleteCharAt(sql2.length() - 1);
    sql2.append("RETURNING id,name,path,groupId");
    List<Image> updatedImageList = parseImage(jdbcTemplate.queryForList(sql2.toString()));

    StringBuilder sql3 = new StringBuilder();
    sql3.append("INSERT OR IGNORE INTO image_tag(image_id,tag_id) VALUES");
    for (ImageTag imageTag : images) {
      Image image = containsTag(imageTag, updatedImageList);
      if (image != null) {
        List<Tag> tags = getTags(imageTag.getTags());
        for (Tag tag : tags) {
          sql3.append("(").append(image.getId()).append(",").append(tag.getId()).append("),");
        }
      }
    }
    sql3.deleteCharAt(sql3.length() - 1);
    if (updatedImageList.isEmpty()) {
      return;
    }
    jdbcTemplate.update(sql3.toString());

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

  private Image containsTag(ImageTag imageTag, List<Image> updatedImageList) {
    for (Image image : updatedImageList) {
      if (image.getName().equals(imageTag.getImage().getName())) {
        return image;
      }
    }
    return null;
  }
}
