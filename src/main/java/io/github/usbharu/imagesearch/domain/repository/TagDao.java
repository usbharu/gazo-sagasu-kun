package io.github.usbharu.imagesearch.domain.repository;

import io.github.usbharu.imagesearch.domain.model.Tag;
import io.github.usbharu.imagesearch.domain.model.TagCount;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TagDao {


  Log log = LogFactory.getLog(TagDao.class);
  @Autowired
  private JdbcTemplate jdbcTemplate;


  public List<Tag> findAll() {
    List<Map<String, Object>> maps = jdbcTemplate.queryForList("SELECT * FROM tag");
    List<Tag> result = new ArrayList<>();
    for (Map<String, Object> map : maps) {
      result.add(new Tag(((Integer) map.get("id")), (String) map.get("name")));
    }
    return result;
  }

  public Tag findById(int id) {
    Map<String, Object> maps = jdbcTemplate.queryForMap("SELECT * FROM tag WHERE id = ?", id);

    return new Tag(((Integer) maps.get("id")), (String) maps.get("name"));

  }

  public List<Tag> findByName(String name) {
    List<Map<String, Object>> maps =
        jdbcTemplate.queryForList("SELECT * FROM tag WHERE name = ?", name);
    List<Tag> result = new ArrayList<>();
    for (Map<String, Object> map : maps) {
      result.add(new Tag(((Integer) map.get("id")), (String) map.get("name")));
    }
    return result;
  }

  public int insertOne(String tag) {
    log.debug("insertOne: " + tag);
    return jdbcTemplate.update(
        "INSERT INTO tag (name) SELECT ? WHERE NOT EXISTS(SELECT 1 FROM tag WHERE name = ?)", tag,
        tag);
  }

  public Tag insertOneWithReturnTag(String tag) {
    insertOne(tag);
    Map<String, Object> stringObjectMap =
        jdbcTemplate.queryForMap("SELECT id,name FROM tag WHERE name = ?;", tag);
    return new Tag(((Integer) stringObjectMap.get("id")), (String) stringObjectMap.get("name"));
  }

  public int deleteOne(Tag tag) {
    return jdbcTemplate.update("DELETE FROM tag WHERE id = ?", tag.getId());
  }

  public int deleteById(int id) {
    return jdbcTemplate.update("DELETE FROM tag WHERE id = ?", id);
  }

  public int deleteByName(String name) {
    return jdbcTemplate.update("DELETE FROM tag WHERE name = ?", name);
  }

  public Tag selectRandomOne() {
    Map<String, Object> stringObjectMap =
        jdbcTemplate.queryForMap("SELECT id,name FROM tag ORDER BY RANDOM() LIMIT 1");
    return new Tag(((Integer) stringObjectMap.get("id")), (String) stringObjectMap.get("name"));
  }

  public List<TagCount> tagCount() {
    List<TagCount> result = new ArrayList<>();
    List<Map<String, Object>> maps = jdbcTemplate.queryForList(
        "SELECT tag_id,tag.name as tag_name,COUNT(tag_id) as tag_count from image_tag JOIN tag on tag_id = tag.id GROUP BY tag_id ORDER BY COUNT(tag_id) DESC");
    for (Map<String, Object> map : maps) {
      result.add(new TagCount((Integer) map.get("tag_count"),
          new Tag(((Integer) map.get("tag_id")), (String) map.get("tag_name"))));
    }
    return result;
  }

  public List<TagCount> tagCount(int limit) {
    List<TagCount> result = new ArrayList<>();
    List<Map<String, Object>> maps = jdbcTemplate.queryForList(
        "SELECT tag_id,tag.name as tag_name,COUNT(tag_id) as tag_count from image_tag JOIN tag on tag_id = tag.id GROUP BY tag_id ORDER BY COUNT(tag_id) DESC LIMIT ?",
        limit);
    for (Map<String, Object> map : maps) {
      result.add(new TagCount((Integer) map.get("tag_count"),
          new Tag(((Integer) map.get("tag_id")), (String) map.get("tag_name"))));
    }
    return result;
  }
}
