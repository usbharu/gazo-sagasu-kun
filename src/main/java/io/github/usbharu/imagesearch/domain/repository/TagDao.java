package io.github.usbharu.imagesearch.domain.repository;

import io.github.usbharu.imagesearch.domain.model.Tag;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TagDao {

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

  public List<Tag> findAllOrderByNameAsc() {
    List<Map<String, Object>> maps = jdbcTemplate.queryForList("SELECT * FROM tag ORDER BY name ASC");
    List<Tag> result = new ArrayList<>();
    for (Map<String, Object> map : maps) {
      result.add(new Tag(((Integer) map.get("id")), (String) map.get("name")));
    }
    return result;
  }

  public List<Tag> findAllOrderByNameDesc(){
    List<Map<String, Object>> maps = jdbcTemplate.queryForList("SELECT * FROM tag ORDER BY name DESC");
    List<Tag> result = new ArrayList<>();
    for (Map<String, Object> map : maps) {
      result.add(new Tag(((Integer) map.get("id")), (String) map.get("name")));
    }
    return result;
  }

  public List<Tag> findAllOrderByIdDesc(){
    List<Map<String, Object>> maps = jdbcTemplate.queryForList("SELECT * FROM tag ORDER BY id DESC");
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

}
