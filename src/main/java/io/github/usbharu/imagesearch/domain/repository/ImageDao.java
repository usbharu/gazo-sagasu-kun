package io.github.usbharu.imagesearch.domain.repository;

import static io.github.usbharu.imagesearch.util.ImageTagUtil.parseImage;
import static io.github.usbharu.imagesearch.util.ImageTagUtil.parseImages;

import io.github.usbharu.imagesearch.domain.model.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("ImageDao")
public class ImageDao {

  Log log = LogFactory.getLog(ImageDao.class);
  @Autowired
  private JdbcTemplate jdbcTemplate;

  public List<Image> findAll() {
    log.debug("findAll");
    List<Map<String, Object>> maps = jdbcTemplate.queryForList(
        "SELECT id as image_id,name as image_name,path as image_path,groupId as image_group FROM image");
    List<Image> result = parseImages(maps);
    log.debug("Success to findAll : " + result.size());
    return result;
  }

  public List<Image> findAllOrderByNameAsc() {
    log.debug("findAllOrderByNameAsc");
    List<Map<String, Object>> maps =
        jdbcTemplate.queryForList("SELECT id as image_id,name as image_name,path as image_path,groupId as image_group FROM image ORDER BY name ASC");
    List<Image> result = parseImages(maps);
    log.debug("Success to findAllOrderByNameAsc : " + result.size());
    return result;
  }

  public List<Image> findAllOrderByNameDesc() {
    log.debug("findAllOrderByNameDesc");
    List<Map<String, Object>> maps =
        jdbcTemplate.queryForList("SELECT id as image_id,name as image_name,path as image_path,groupId as image_group FROM image ORDER BY name DESC");
    List<Image> result = parseImages(maps);
    log.debug("Success to findAllOrderByNameDesc : " + result.size());
    return result;
  }

  public List<Image> findAllOrderByIdDesc() {
    log.debug("findAllOrderByIdDesc");
    List<Map<String, Object>> maps =
        jdbcTemplate.queryForList("SELECT id as image_id,name as image_name,path as image_path,groupId as image_group FROM image ORDER BY id DESC");
    List<Image> result = parseImages(maps);
    log.debug("Success to findAllOrderByIdDesc : " + result.size());
    return result;
  }

  public List<Image> findByName(String name) {
    log.debug("findByName name:" + name);
    List<Map<String, Object>> maps =
        jdbcTemplate.queryForList("SELECT id as image_id,name as image_name,path as image_path,groupId as image_group FROM image WHERE name = ?", name);
    List<Image> result = parseImages(maps);
    log.debug("Success to findByName : " + result.size());
    return result;
  }

  public Image findByUrl(String url) {
    log.debug("findByUrl url:" + url);
    Map<String, Object> maps = jdbcTemplate.queryForMap("SELECT id as image_id,name as image_name,path as image_path,groupId as image_group FROM image WHERE path = ?", url);
    Image image = parseImage(maps);
    log.debug("Success to findByUrl : " + image);
    return image;
  }

  public Image findById(int id) {
    log.debug("findById id:" + id);
    Map<String, Object> stringObjectMap =
        jdbcTemplate.queryForMap("SELECT id as image_id,name as image_name,path as image_path,groupId as image_group FROM image WHERE id = ?", id);
    Image image = parseImage(stringObjectMap);
    log.debug("Success to findById id:" + image);
    return image;
  }

  public int insertOne(Image image) {
    log.debug("insertOne image:" + image);
    int update =
        jdbcTemplate.update("INSERT OR IGNORE INTO main.image(name, path, groupId) VALUES (?,?,?)",
            image.getName(), image.getPath(), image.getGroup());
    log.debug("Success to insertOne : " + update);
    return update;
  }

  public Image insertOneWithReturnImage(Image image) {
    log.debug("insertOneWithReturnImage image:" + image);
    insertOne(image);
    Map<String, Object> stringObjectMap =
        jdbcTemplate.queryForMap("SELECT id,name,path FROM image WHERE path = ?", image.getPath());
    Image result = parseImage(stringObjectMap);
    log.debug("Success to insertOneWithReturnImage : " + result);
    return result;
  }

  public Image selectOne(String url) {

    return findByUrl(url);
  }

  public int deleteOne(String url) {
    log.debug("deleteOne url:" + url);
    int update = jdbcTemplate.update("DELETE FROM image WHERE path = ?", url);
    log.debug("Success to deleteOne : " + update);
    return update;
  }
}
