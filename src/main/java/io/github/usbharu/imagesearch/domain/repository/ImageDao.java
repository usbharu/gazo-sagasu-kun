package io.github.usbharu.imagesearch.domain.repository;

import static io.github.usbharu.imagesearch.util.ImageTagUtil.parseImage;
import static io.github.usbharu.imagesearch.util.ImageTagUtil.parseImages;

import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.validation.StringValidation;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("ImageDao")
public class ImageDao {

  Logger logger = LoggerFactory.getLogger(ImageDao.class);
  @Autowired
  private JdbcTemplate jdbcTemplate;

  public List<Image> findAll() {
    logger.trace("findAll");
    List<Map<String, Object>> maps = jdbcTemplate.queryForList(
        "SELECT id as image_id,name as image_name,path as image_path,groupId as image_group FROM image");
    List<Image> result = parseImages(maps);
    logger.trace("Success to findAll : " + result.size());
    return result;
  }
  public List<Image> findByName(String name) {
    StringValidation.requireNonNullAndNonBlank(name,"name is null or blank");
    logger.trace("findByName name:" + name);
    List<Map<String, Object>> maps =
        jdbcTemplate.queryForList(
            "SELECT id as image_id,name as image_name,path as image_path,groupId as image_group FROM image WHERE name = ?",
            name);
    List<Image> result = parseImages(maps);
    logger.trace("Success to findByName : " + result.size());
    return result;
  }

  public Image findByUrl(String url) {
    StringValidation.requireNonNullAndNonBlank(url,"url is null or blank");
    logger.trace("findByUrl url:" + url);
    Map<String, Object> maps = jdbcTemplate.queryForMap(
        "SELECT id as image_id,name as image_name,path as image_path,groupId as image_group FROM image WHERE path = ?",
        url);
    Image image = parseImage(maps);
    logger.trace("Success to findByUrl : " + image);
    return image;
  }

  public Image findById(int id) {
    logger.trace("findById id:" + id);
    Map<String, Object> stringObjectMap =
        jdbcTemplate.queryForMap(
            "SELECT id as image_id,name as image_name,path as image_path,groupId as image_group FROM image WHERE id = ?",
            id);
    Image image = parseImage(stringObjectMap);
    logger.trace("Success to findById id:" + image);
    return image;
  }

  public int insertOne(Image image) {
    Objects.requireNonNull(image);
    logger.trace("insertOne image:" + image);
    int update =
        jdbcTemplate.update("INSERT OR IGNORE INTO main.image(name, path, groupId) VALUES (?,?,?)",
            image.getName(), image.getPath(), image.getGroup());
    logger.trace("Success to insertOne : " + update);
    return update;
  }

  public Image insertOneWithReturnImage(Image image) {
    Objects.requireNonNull(image,"Image is null");
    logger.trace("insertOneWithReturnImage image:" + image);
    insertOne(image);
    Map<String, Object> stringObjectMap =
        jdbcTemplate.queryForMap("SELECT id,name,path FROM image WHERE path = ?", image.getPath());
    Image result = parseImage(stringObjectMap);
    logger.trace("Success to insertOneWithReturnImage : " + result);
    return result;
  }

  public Image selectOne(String url) {
    return findByUrl(url);
  }

  public int deleteOne(String url) {
    StringValidation.requireNonNullAndNonBlank(url,"url is null or blank");
    logger.trace("deleteOne url:" + url);
    int update = jdbcTemplate.update("DELETE FROM image WHERE path = ?", url);
    logger.trace("Success to deleteOne : " + update);
    return update;
  }
}
