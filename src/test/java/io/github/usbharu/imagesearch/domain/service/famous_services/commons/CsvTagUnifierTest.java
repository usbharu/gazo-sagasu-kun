package io.github.usbharu.imagesearch.domain.service.famous_services.commons;

import static org.junit.jupiter.api.Assertions.*;

import io.github.usbharu.imagesearch.domain.model.ImageMetadata;
import io.github.usbharu.imagesearch.domain.model.Tag;
import io.github.usbharu.imagesearch.domain.model.Tags;
import io.github.usbharu.imagesearch.domain.service.scan.UnifierTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CsvTagUnifierTest extends UnifierTest {

  private String defaultPath;

  @BeforeEach
  void setUp() {
    defaultPath = getClass().getClassLoader().getResource("testData/unifier.csv").getPath();
    super.unifier = new CsvTagUnifier();
  }

  @Override
  @Test
  protected void unify_ImageMetadata_unifiedImageMetadata() {
    CsvTagUnifier csvTagUnifier = (CsvTagUnifier) unifier;
    csvTagUnifier.setPath(defaultPath);
    csvTagUnifier.init();
    super.unify_ImageMetadata_unifiedImageMetadata();
  }

  @Override
  @Test
  protected void unify_null_null() {
    CsvTagUnifier csvTagUnifier = (CsvTagUnifier) super.unifier;
    csvTagUnifier.setPath(defaultPath);
    csvTagUnifier.init();
    super.unify_null_null();
  }

  @Test
  void unify_planeUnifier_unifying() {
    CsvTagUnifier csvTagUnifier = (CsvTagUnifier) super.unifier;
    csvTagUnifier.setPath(defaultPath);
    csvTagUnifier.init();
    Tags tags = new Tags();
    tags.add(new Tag("unify1"));
    tags.add(new Tag("test2"));
    ImageMetadata actual = csvTagUnifier.unify(tags);
    assertEquals(2, ((Tags) actual).size());
    assertEquals("unified", ((Tags) actual).get(0).getName());
    assertEquals("testUnified", ((Tags) actual).get(1).getName());
  }

  @Test
  void unify_regexUnifier_unifying() {
    CsvTagUnifier csvTagUnifier = (CsvTagUnifier) super.unifier;
    csvTagUnifier.setPath(defaultPath);
    csvTagUnifier.init();
    Tags tags = new Tags();
    tags.add(new Tag("fd"));
    tags.add(new Tag("83zMYiE"));
    ImageMetadata actual = csvTagUnifier.unify(tags);
    assertEquals(2, ((Tags) actual).size());
    assertEquals("testUnifiedRegex", ((Tags) actual).get(0).getName());
    assertEquals("83zMYiE", ((Tags) actual).get(1).getName());
  }

  @Test
  void init_unifierCsv_makePattern() {
    CsvTagUnifier csvTagUnifier = (CsvTagUnifier) super.unifier;
    csvTagUnifier.setPath(defaultPath);
    csvTagUnifier.init();
    assertEquals(2, csvTagUnifier.planeUnifier.size());
    assertEquals(1, csvTagUnifier.regexUnifier.size());
  }

  @Test
  void init_nullPath_doNothing() {
    CsvTagUnifier csvTagUnifier = (CsvTagUnifier) super.unifier;
    csvTagUnifier.setPath(null);
    csvTagUnifier.init();
    assertEquals(0, csvTagUnifier.planeUnifier.size());
    assertEquals(0, csvTagUnifier.regexUnifier.size());
  }

  @Test
  void init_emptyCsvFilter_doNothing() {
    CsvTagUnifier csvTagUnifier = (CsvTagUnifier) super.unifier;
    csvTagUnifier.setPath(getClass().getClassLoader().getResource("testData/unifier_empty.csv").getPath());
    csvTagUnifier.init();
    assertEquals(0, csvTagUnifier.planeUnifier.size());
    assertEquals(0, csvTagUnifier.regexUnifier.size());
  }

  @Test
  void init_invalidRegex_skipRegex() {
    CsvTagUnifier csvTagUnifier = (CsvTagUnifier) super.unifier;
    csvTagUnifier.setPath(getClass().getClassLoader().getResource("testData/unifier_error_regex.csv").getPath());
    csvTagUnifier.init();
    assertEquals(0, csvTagUnifier.planeUnifier.size());
    assertEquals(0, csvTagUnifier.regexUnifier.size());
  }

  @Test
  void init_invalidPath_skipInit() {
    CsvTagUnifier csvTagUnifier = (CsvTagUnifier) super.unifier;
    csvTagUnifier.setPath("CjkGOP");
    assertDoesNotThrow(csvTagUnifier::init);
  }
}
