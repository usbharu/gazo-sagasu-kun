package io.github.usbharu.imagesearch.domain.repository;

import static io.github.usbharu.imagesearch.domain.validation.Validation.require;

import io.github.usbharu.imagesearch.domain.exceptions.TagDatabaseEmptyException;
import io.github.usbharu.imagesearch.domain.model.Tag;
import io.github.usbharu.imagesearch.domain.model.custom.TagCount;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class TagDao {


  private final TagRowMapper tagRowMapper;
  Logger logger = LoggerFactory.getLogger(TagDao.class);
  @Autowired
  private JdbcTemplate jdbcTemplate;

  public TagDao() {
    tagRowMapper = new TagRowMapper();
  }


  public List<Tag> findAll() {
    return jdbcTemplate.query("SELECT id as tag_id,name as tag_name FROM tag", tagRowMapper);
  }

  public Tag findById(int id) {
    require().positive(id);
    return jdbcTemplate.queryForObject("SELECT id as tag_id,name as tag_name FROM tag WHERE tag_id = ?", tagRowMapper,id);

  }

  public List<Tag> findByName(String name) {
    require().nonNullAndNonBlank(name, "name is null or blank");
    return jdbcTemplate.query("SELECT id as tag_id,name as tag_name FROM tag WHERE name = ?",
        tagRowMapper, name);
  }

  public int insertOne(String tag) {
    require().nonNullAndNonBlank(tag, "Tag is null or blank");
    logger.debug("insertOne: {}", tag);
    return jdbcTemplate.update(
        "INSERT INTO tag (name) SELECT ? WHERE NOT EXISTS(SELECT 1 FROM tag WHERE name = ?)", tag,
        tag);
  }

  public Tag insertOneWithReturnTag(String tag) {
    require().nonNullAndNonBlank(tag, "Tag is Null or blank");
    insertOne(tag);

    return jdbcTemplate.queryForObject(
        "SELECT id as tag_id,name as tag_name FROM tag WHERE name = ?;", tagRowMapper, tag);

  }

  public int deleteOne(Tag tag) {
    Objects.requireNonNull(tag, "Tag is Null");
    return jdbcTemplate.update("DELETE FROM tag WHERE id = ? AND name = ?", tag.getId(),tag.getName());
  }

  public int deleteById(int id) {
    require().positive(id,"Id is negative or zero");
    return jdbcTemplate.update("DELETE FROM tag WHERE id = ?", id);
  }

  public int deleteByName(String name) {
    require().nonNullAndNonBlank(name, "Name is null or blank");
    return jdbcTemplate.update("DELETE FROM tag WHERE name = ?", name);
  }

  public Tag selectRandomOne() {
    try {

      return jdbcTemplate.queryForObject(
          "SELECT id as tag_id,name as tag_name\n"
              + "FROM tag\n"
              + "WHERE NOT name LIKE '--%--"
              + "'\n"
              + "  AND NOT 'NON"
              + "E'\n"
              + "ORDER BY RANDOM"
              + "()\n"
              + "LIMIT 1", tagRowMapper);
    }catch (EmptyResultDataAccessException e){
      throw new TagDatabaseEmptyException("Tag Database is Empty",e);
    }
  }

  public List<TagCount> tagCount() {
    List<TagCount> result = new ArrayList<>();
    List<Map<String, Object>> maps = jdbcTemplate.queryForList(
        "SELECT tag_id, tag.name as tag_name, COUNT(tag_id) as tag_count\n"
            + "from image_tag\n"
            + "         JOIN tag on tag_id = tag.id\n"
            + "WHERE NOT tag_name LIKE '--%--'\n"
            + "  AND NOT 'NONE'\n"
            +
            "GROUP BY tag_id\n"
            + "ORDER BY COUNT(tag_id) DESC");
    for (Map<String, Object> map : maps) {
      result.add(new TagCount((Integer) map.get("tag_count"),
          new Tag(((Integer) map.get("tag_id")), (String) map.get("tag_name"))));
    }
    return result;
  }

  public List<TagCount> tagCount(int limit) {
    if (limit <= 0) {
      throw new IllegalArgumentException("Limit is negative or zero limit :" + limit);
    }
    List<TagCount> result = new ArrayList<>();
    List<Map<String, Object>> maps = jdbcTemplate.queryForList(
        "SELECT tag_id, tag.name as tag_name, COUNT(tag_id) as tag_count\n"
            + "from image_tag\n"
            + "         JOIN tag on tag_id = tag.id\n"
            + "WHERE tag_name NOT LIKE '--%--'\n"
            + "  AND NOT 'NONE'\n"
            +
            "GROUP BY tag_id\n"
            + "ORDER BY COUNT(tag_id) DESC\n"
            + "LIMIT ?",
        limit);
    for (Map<String, Object> map : maps) {
      result.add(new TagCount((Integer) map.get("tag_count"),
          new Tag(((Integer) map.get("tag_id")), (String) map.get("tag_name"))));
    }
    return result;
  }

  public static class TagRowMapper implements RowMapper<Tag>{

    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new Tag(rs.getInt("tag_id"),rs.getString("tag_name"));
    }
  }
}
