package io.github.usbharu.imagesearch.util;

import static io.github.usbharu.imagesearch.util.ImageTagUtil.parseTag;
import static io.github.usbharu.imagesearch.util.ImageTagUtil.parseTags;
import static org.junit.jupiter.api.Assertions.*;

import io.github.usbharu.imagesearch.domain.model.Tag;
import io.github.usbharu.imagesearch.domain.model.Tags;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ImageTagUtilTest {

  private final static Map<String, Object> tagMap = Map.of("id",1,"name","tag1");
  private final static Map<String, Object> imageMap = Map.of();
  private final static Map<String, Object> imageWithTagMap = Map.of();

  private final static List<Map<String, Object>> tagMapList =
      List.of(Map.of("id", 1, "name", "tag1"), Map.of("id", 2, "name", "tag2"));
  private final static List<Map<String, Object>> imageMapList = List.of();
  private final static List<Map<String, Object>> imageWithTagMapList = List.of();


  @Test
  void parseTags_TagsMap_returnTags() {
    assertDoesNotThrow(() -> parseTags(tagMapList));
    Tags tags = parseTags(tagMapList);
    assertEquals(tagMapList.size(), tags.size());
    assertEquals("tag2", tags.get(1).getName());
    assertEquals(2, tags.get(1).getId());
  }

  @Test
  void parseTags_EmptyList_returnEmptyTags() {
    assertDoesNotThrow(() -> parseTags(List.of()));
    Tags empty = parseTags(List.of());
    assertNotNull(empty);
    assertEquals(0, empty.size());
  }

  @Test
  void parseTags_Null_throwNullPointException() {
    assertThrows(NullPointerException.class, () -> parseTags(null));
  }

  @Test
  void parseTags_HasNullList_throwNullPointException() {
    List<Map<String,Object>> list = new ArrayList<>();
    list.add(null);
    assertThrows(NullPointerException.class,()->parseTags(list));
  }

  @Test
  void parseTag_TagMap_returnTag() {
    assertDoesNotThrow(()-> parseTag(tagMap));
    Tag tag = parseTag(tagMap);
    assertNotNull(tag);
    assertEquals(1,tag.getId());
    assertEquals("tag1",tag.getName());
  }
}
