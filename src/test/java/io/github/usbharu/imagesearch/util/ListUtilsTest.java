package io.github.usbharu.imagesearch.util;

import static io.github.usbharu.imagesearch.util.ListUtils.getOr;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ListUtilsTest {

  @Test
  void getOr_getFromIndex_returnElement() {
    List<String> list = List.of("a", "b", "c", "d");
    String actual = getOr(list, 2, "a");
    String expected = "c";
    assertEquals(expected, actual);
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 100, 3892843})
  void getOr_getFromIllegalIndex_returnDefault(int n) {
    List<String> list = List.of("a", "b", "c", "d");
    String actual = getOr(list, n, "a");
    String expected = "a";
    assertEquals(expected, actual);
  }
}
