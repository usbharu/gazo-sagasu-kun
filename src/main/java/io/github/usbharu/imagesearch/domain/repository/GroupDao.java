package io.github.usbharu.imagesearch.domain.repository;

import io.github.usbharu.imagesearch.domain.model.Group;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GroupDao {

  @Autowired
  JdbcTemplate jdbcTemplate;

  public List<Group> findAll() {
    List<Map<String, Object>> maps = jdbcTemplate.queryForList("SELECT * FROM groupId");
    return parseMapList(maps);
  }

  public Group findById(int id) {
    List<Map<String, Object>> maps =
        jdbcTemplate.queryForList("SELECT * FROM groupId WHERE id = ?", id);
    return parseMapList(maps).get(0);
  }

  public Group findByName(String name) {
    Map<String, Object> maps =
        jdbcTemplate.queryForMap("SELECT * FROM groupId WHERE name = ?", name);
    return parseMap(maps);
  }

  public int insertOne(String name) {
    return jdbcTemplate.update(
        "INSERT INTO groupId (name) SELECT ? WHERE NOT EXISTS(SELECT 1 FROM groupId WHERE name = ?)",
        name, name);
  }

  public Group insertOneWithReturnGroup(String name) {
    insertOne(name);
    List<Map<String, Object>> maps =
        jdbcTemplate.queryForList("SELECT id,name FROM groupId WHERE name = ?;", name);
    return parseMapList(maps).get(0);
  }

  public int deleteOne(int id) {
    return jdbcTemplate.update("DELETE FROM groupId WHERE id = ?", id);
  }

  public int deleteOne(String name) {
    return jdbcTemplate.update("DELETE FROM groupId WHERE name = ?", name);
  }

  public int updateOne(int id, String name) {
    return jdbcTemplate.update("UPDATE groupId SET name = ? WHERE id = ?", name, id);
  }

  private Group parseMap(Map<String, Object> map) {
    return new Group((int) map.get("id"), (String) map.get("name"));
  }

  private List<Group> parseMapList(List<Map<String, Object>> maps) {
    List<Group> result = new ArrayList<>();
    for (Map<String, Object> map : maps) {
      result.add(parseMap(map));
    }
    return result;
  }
}
