package io.github.usbharu.imagesearch.domain.repository;

import static io.github.usbharu.imagesearch.util.ImageTagUtil.parseTag;
import static io.github.usbharu.imagesearch.util.ImageTagUtil.parseTags;

import io.github.usbharu.imagesearch.domain.model.Tag;
import io.github.usbharu.imagesearch.domain.model.TagCount;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TagDao {


  Logger logger = LoggerFactory.getLogger(TagDao.class);
  @Autowired
  private JdbcTemplate jdbcTemplate;


  public List<Tag> findAll() {
    List<Map<String, Object>> maps = jdbcTemplate.queryForList("SELECT * FROM tag");
    return parseTags(maps);
  }

  public Tag findById(int id) {
    Map<String, Object> maps = jdbcTemplate.queryForMap("SELECT * FROM tag WHERE id = ?", id);
    return parseTag(maps);

  }

  public List<Tag> findByName(String name) {
    List<Map<String, Object>> maps =
        jdbcTemplate.queryForList("SELECT * FROM tag WHERE name = ?", name);
    return parseTags(maps);
  }

  public int insertOne(String tag) {
    logger.debug("insertOne: {}", tag);
    return jdbcTemplate.update(
        "INSERT INTO tag (name) SELECT ? WHERE NOT EXISTS(SELECT 1 FROM tag WHERE name = ?)", tag,
        tag);
  }

  public Tag insertOneWithReturnTag(String tag) {
    insertOne(tag);
    Map<String, Object> stringObjectMap =
        jdbcTemplate.queryForMap("SELECT id,name FROM tag WHERE name = ?;", tag);
    return parseTag(stringObjectMap);
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
    System.out.println("findAll() = " + findAll());
    Map<String, Object> stringObjectMap =
        jdbcTemplate.queryForMap(
            "SELECT id, name\n"
                + "FROM tag\n"
                + "WHERE NOT name LIKE '--%--"
                + "'\n"
                + "  AND NOT 'NON"
                + "E'\n"
                + "ORDER BY RANDOM"
                + "()\n"
                + "LIMIT 1");
    return parseTag(stringObjectMap);
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
}
