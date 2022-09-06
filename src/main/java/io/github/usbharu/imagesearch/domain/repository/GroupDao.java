package io.github.usbharu.imagesearch.domain.repository;

import io.github.usbharu.imagesearch.domain.model.Group;
import io.github.usbharu.imagesearch.domain.validation.StringValidation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GroupDao {

  final
  JdbcTemplate jdbcTemplate;

  public GroupDao(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

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
    StringValidation.requireNonNullAndNonBlank(name,"Name is Null or blank");
    Map<String, Object> maps =
        jdbcTemplate.queryForMap("SELECT * FROM groupId WHERE name = ?", name);
    return parseMap(maps);
  }

  public int insertOne(String name) {
    StringValidation.requireNonNullAndNonBlank(name,"Name is Null or blank");
    return jdbcTemplate.update(
        "INSERT INTO groupId (name) SELECT ? WHERE NOT EXISTS(SELECT 1 FROM groupId WHERE name = ?)",
        name, name);
  }

  public Group insertOneWithReturnGroup(String name) {
    StringValidation.requireNonNullAndNonBlank(name,"Name is null or blank");
    insertOne(name);
    Map<String, Object> maps =
        jdbcTemplate.queryForMap("SELECT id,name FROM groupId WHERE name = ?;", name);
    return parseMap(maps);
  }

  public int deleteOne(int id) {
    return jdbcTemplate.update("DELETE FROM groupId WHERE id = ?", id);
  }

  public int deleteOne(String name) {
    StringValidation.requireNonNullAndNonBlank(name,"Name is null or blank");
    return jdbcTemplate.update("DELETE FROM groupId WHERE name = ?", name);
  }

  public int updateOne(int id, String name) {
    StringValidation.requireNonNullAndNonBlank(name,"Name is null or blank");
    return jdbcTemplate.update("UPDATE groupId SET name = ? WHERE id = ?", name, id);
  }

  private Group parseMap(Map<String, Object> map) {
    Objects.requireNonNull(map,"Group Map is Null");
    return new Group((int) map.get("id"), (String) map.get("name"));
  }

  private List<Group> parseMapList(List<Map<String, Object>> maps) {
    Objects.requireNonNull(maps,"Group Map List is Null");
    List<Group> result = new ArrayList<>();
    for (Map<String, Object> map : maps) {
      result.add(parseMap(map));
    }
    return result;
  }
}
