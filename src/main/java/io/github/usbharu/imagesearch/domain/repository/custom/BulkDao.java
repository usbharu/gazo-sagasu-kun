package io.github.usbharu.imagesearch.domain.repository.custom;

import static io.github.usbharu.imagesearch.util.ImageTagUtil.getTagsNoNull;
import static io.github.usbharu.imagesearch.util.ImageTagUtil.parseImages;

import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.model.Tag;
import io.github.usbharu.imagesearch.domain.model.Tags;
import io.github.usbharu.imagesearch.domain.repository.GroupDao;
import io.github.usbharu.imagesearch.domain.repository.TagDao;
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
public class BulkDao {

  private final JdbcTemplate jdbcTemplate;

  private final Logger logger = LoggerFactory.getLogger(BulkDao.class);
  private final Tag none = new Tag(1, "NONE");

  /**
   * Autowired用のコンストラクタ
   *
   * @param jdbcTemplate jdbcテンプレート
   * @param tagDao       タグのDAO
   * @param groupDao     グループのDAO
   */
  @Autowired
  public BulkDao(JdbcTemplate jdbcTemplate, TagDao tagDao, GroupDao groupDao) {
    this.jdbcTemplate = jdbcTemplate;
  }

  /**
   * 指定数ごとに分割した後まとめてインサートします。
   *
   * @param images インサートする画像
   * @param split  分割する枚数画像のリスト
   */
  public void insertSplit(List<Image> images, int split) {
    logger.info("Images :{} split:{}", images.size(), split);
    int len = (images.size() / split);
    len += images.size()%split==0?0:1;
    for (int i = 0; i < len; i++) {
      logger.debug("insert range: {}-{}", split * i, split * (i + 1));
      insert(images.subList(split * i, Math.min(images.size(), split * (i + 1))));
    }
  }

  /**
   * 100枚ごと二分割したものをまとめてインサートします。
   *
   * @param images インサートする画像のリスト
   */
  public void insertSplit(List<Image> images) {
    insertSplit(images, 100);
  }

  /**
   * まとめてインサートする。
   *
   * @param images インサートする画像のリスト
   */
  public void insert(List<Image> images) {
    logger.debug("Insert Image :{}",images.size());
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
    tagSql.append("('NONE'),");
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
    imageSql.append(
        "RETURNING id as image_id,name as image_name,path as image_path,groupId as image_group");
    List<Image> updatedImageList = parseImages(jdbcTemplate.queryForList(imageSql.toString()));

    logger.debug("{} images have been updated.", updatedImageList.size());

    StringBuilder imageTagSql = new StringBuilder();
    imageTagSql.append("INSERT OR IGNORE INTO image_tag(image_id,tag_id) VALUES");
    List<Image> imageList = parseImages(jdbcTemplate.queryForList(
        "SELECT id as image_id,name as image_name,path as image_path,groupId as image_group FROM main.image"));
    if (imageList.isEmpty()) {
      return;
    }
    for (Image image : images) {
      int id = -1;
      for (Image image1 : imageList) {
        if (image1.getPath().equals(image.getPath())) {
          id = image1.getId();
          break;
        }
      }

      List<Tag> tags = getTagsFromDB(getTagsNoNull(image));
      for (Tag tag : tags) {
        imageTagSql.append("(").append(id).append(",").append(tag.getId()).append("),");
      }
    }
    imageTagSql.deleteCharAt(imageTagSql.length() - 1);

    jdbcTemplate.update(imageTagSql.toString());

  }

  public void delete() {
    logger.info("delete all");
    synchronized (jdbcTemplate){
      jdbcTemplate.update("DELETE FROM main.image NOT INDEXED ");
      jdbcTemplate.update("DELETE FROM main.groupId NOT INDEXED ");
      jdbcTemplate.update("DELETE FROM main.image_tag NOT INDEXED ");
      jdbcTemplate.update("DELETE FROM main.tag NOT INDEXED ");
      jdbcTemplate.update("DELETE FROM main.sqlite_sequence NOT INDEXED ");
    }

    logger.info("complete delete all");
  }

  private List<Tag> getTagsFromDB(List<Tag> tags) {
    if (tags.isEmpty()) {
      return List.of(none);
    }
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