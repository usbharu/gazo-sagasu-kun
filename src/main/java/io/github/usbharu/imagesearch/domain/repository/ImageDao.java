package io.github.usbharu.imagesearch.domain.repository;

import static io.github.usbharu.imagesearch.domain.validation.Validation.require;

import io.github.usbharu.imagesearch.domain.model.Image;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository("ImageDao")
public class ImageDao {

  private final ImageRowMapper imageRowMapper;
  Logger logger = LoggerFactory.getLogger(ImageDao.class);
  @Autowired
  private JdbcTemplate jdbcTemplate;

  public ImageDao() {
    imageRowMapper = new ImageRowMapper();
  }

  public List<Image> findAll() {
    logger.trace("findAll");
    return jdbcTemplate.query(
        "SELECT id as image_id,name as image_name,path as image_path,groupId as image_group FROM image",
        imageRowMapper);
  }

  public List<Image> findByName(String name) {
    require().nonNullAndNonBlank(name, "name is null or blank");
    logger.trace("findByName name:" + name);
    return
        jdbcTemplate.query(
            "SELECT id as image_id,name as image_name,path as image_path,groupId as image_group FROM image WHERE name = ?",
            imageRowMapper,
            name);

  }

  public Image findByUrl(String url) {
    require().nonNullAndNonBlank(url, "url is null or blank");
    logger.trace("findByUrl url:" + url);
    try {

      return jdbcTemplate.queryForObject(
          "SELECT id as image_id,name as image_name,path as image_path,groupId as image_group FROM image WHERE path = ?",
          imageRowMapper,
          url);
    } catch (EmptyResultDataAccessException e) {
      logger.warn(url + " was not found.", e);
      return null;
    }
  }

  public Image findById(int id) {
    require().positive(id);
    logger.trace("findById id:" + id);
    try {
      return jdbcTemplate.queryForObject(
          "SELECT id as image_id,name as image_name,path as image_path,groupId as image_group FROM image WHERE id = ?",
          imageRowMapper,
          id);
    } catch (EmptyResultDataAccessException e) {
      logger.warn(id + " was not found", e);
      return null;
    }
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
    Objects.requireNonNull(image, "Image is null");
    logger.trace("insertOneWithReturnImage image:" + image);
    insertOne(image);
    return jdbcTemplate.queryForObject(
        "SELECT id as image_id,name as image_name,path as image_path,groupId as image_group FROM image WHERE path = ?",
        imageRowMapper,
        image.getPath());
  }

  public Image selectOne(String url) {
    return findByUrl(url);
  }

  public int deleteOne(String url) {
    require().nonNullAndNonBlank(url, "url is null or blank");
    logger.trace("deleteOne url:" + url);
    int update = jdbcTemplate.update("DELETE FROM image WHERE path = ?", url);
    logger.trace("Success to deleteOne : " + update);
    return update;
  }

  public static class ImageRowMapper implements RowMapper<Image> {

    @Override
    public Image mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new Image(rs.getInt("image_id"), rs.getString("image_name"),
          rs.getString("image_path"), rs.getInt("image_group"));
    }
  }
}
