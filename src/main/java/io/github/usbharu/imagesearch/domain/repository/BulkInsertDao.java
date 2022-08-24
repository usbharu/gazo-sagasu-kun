package io.github.usbharu.imagesearch.domain.repository;

import static io.github.usbharu.imagesearch.util.ImageTagUtil.getTagsNoNull;
import static io.github.usbharu.imagesearch.util.ImageTagUtil.parseImages;

import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.model.ImageMetadata;
import io.github.usbharu.imagesearch.domain.model.Tag;
import io.github.usbharu.imagesearch.domain.model.Tags;
import io.github.usbharu.imagesearch.util.ImageTagUtil;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * データベースにまとめてインサートするDAO
 */
@Repository
public class BulkInsertDao {

  private final JdbcTemplate jdbcTemplate;

  private final TagDao tagDao;

  private final GroupDao groupDao;

  /**
   * Autowired用のコンストラクタ
   *
   * @param jdbcTemplate jdbcテンプレート
   * @param tagDao       タグのDAO
   * @param groupDao     グループのDAO
   */
  @Autowired
  public BulkInsertDao(JdbcTemplate jdbcTemplate, TagDao tagDao, GroupDao groupDao) {
    this.jdbcTemplate = jdbcTemplate;
    this.tagDao = tagDao;
    this.groupDao = groupDao;
  }

  private final Logger logger = LoggerFactory.getLogger(BulkInsertDao.class);

  /**
   * まとめてインサートする。
   *
   * @param images インサートする画像のリスト
   */
  public void insert(List<Image> images) {
    if (images.isEmpty()) {
      return;
    }
    List<Tag> tagList = new ArrayList<>();
    for (Image image : images) {
      Tags tags = ImageTagUtil.getTagsNoNull(image);

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
    imageSql.append("RETURNING id as image_id,name as image_name,path as image_path,groupId as image_group");
    List<Image> updatedImageList = parseImages(jdbcTemplate.queryForList(imageSql.toString()));

    logger.debug("{} images have been updated.",updatedImageList.size());

    StringBuilder imageTagSql = new StringBuilder();
    imageTagSql.append("INSERT OR IGNORE INTO image_tag(image_id,tag_id) VALUES");
    List<Image> imageList = parseImages(jdbcTemplate.queryForList("SELECT id as image_id,name as image_name,path as image_path,groupId as image_group FROM main.image"));
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

      List<Tag> tags =  getTagsFromDB(getTagsNoNull(image));
      for (Tag tag : tags) {
        imageTagSql.append("(").append(id).append(",").append(tag.getId()).append("),");
      }
    }
    imageTagSql.deleteCharAt(imageTagSql.length()-1);

    jdbcTemplate.update(imageTagSql.toString());

  }
  private List<Tag> getTagsFromDB(List<Tag> tags) {
    StringBuilder sb = new StringBuilder();
    sb.append("SELECT id,name FROM tag WHERE name IN (");
    for (Tag tag : tags) {
      sb.append("'").append(tag.getName()).append("',");
    }
    sb.deleteCharAt(sb.length() - 1);
    sb.append(")");
    return ImageTagUtil.parseTags(jdbcTemplate.queryForList(sb.toString()));
  }
}
