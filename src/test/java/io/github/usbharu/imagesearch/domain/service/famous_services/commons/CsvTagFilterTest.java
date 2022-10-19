package io.github.usbharu.imagesearch.domain.service.famous_services.commons;

import static org.junit.jupiter.api.Assertions.*;

import io.github.usbharu.imagesearch.domain.model.ImageMetadata;
import io.github.usbharu.imagesearch.domain.model.Tag;
import io.github.usbharu.imagesearch.domain.model.Tags;
import io.github.usbharu.imagesearch.domain.service.scan.FilterTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CsvTagFilterTest extends FilterTest {


  private String defaultPath;

  @BeforeEach
  void setUp() {
    defaultPath = getClass().getClassLoader().getResource("testData/filter.csv").getPath();
    super.filter = new CsvTagFilter();
  }

  @Override
  @Test
  protected void filter_ImageMetadata_returnFilteredImageMetadataOrNull() {
    CsvTagFilter filter = (CsvTagFilter) super.filter;
    filter.setPath(defaultPath);
    filter.init();
    super.filter_ImageMetadata_returnFilteredImageMetadataOrNull();
  }

  @Override
  @Test
  protected void filter_null_returnNull() {
    CsvTagFilter filter = (CsvTagFilter) super.filter;
    filter.setPath(defaultPath);
    filter.init();
    super.filter_null_returnNull();
  }

  @Test
  void filter_planeFilter_filtering() {
    CsvTagFilter csvTagFilter = (CsvTagFilter) super.filter;
    csvTagFilter.setPath(defaultPath);
    csvTagFilter.init();
    Tags tags = new Tags();
    tags.add(new Tag("test1"));
    tags.add(new Tag("test2"));
    ImageMetadata filtered = csvTagFilter.filter(tags);
    assertEquals(1,((Tags) filtered).size());
  }

  @Test
  void filter_regexFilter_filtering() {
    CsvTagFilter csvTagFilter = (CsvTagFilter) super.filter;
    csvTagFilter.setPath(defaultPath);
    csvTagFilter.init();
    Tags tags = new Tags();
    tags.add(new Tag("ab"));
    tags.add(new Tag("test2"));
    ImageMetadata filtered = csvTagFilter.filter(tags);
    assertEquals(1,((Tags) filtered).size());
  }

  @Test
  void init_filterCsv_makePattern() {
    CsvTagFilter csvTagFilter = (CsvTagFilter) super.filter;
    csvTagFilter.setPath(defaultPath);
    csvTagFilter.init();
    assertEquals(2,csvTagFilter.simpleFilter.size());
    assertEquals(1,csvTagFilter.patterns.size());
  }

  @Test
  void init_nullPath_doNothing() {
    CsvTagFilter csvTagFilter = (CsvTagFilter) super.filter;
    csvTagFilter.init();
    assertEquals(0,csvTagFilter.simpleFilter.size());
    assertEquals(0,csvTagFilter.patterns.size());
  }

  @Test
  void init_emptyCsvFilter_doNothing() {
    CsvTagFilter csvTagFilter = (CsvTagFilter) super.filter;
    csvTagFilter.setPath(getClass().getClassLoader().getResource("testData/filter_empty.csv").getPath());
    csvTagFilter.init();
    assertEquals(0,csvTagFilter.simpleFilter.size());
    assertEquals(0,csvTagFilter.patterns.size());
  }

  @Test
  void init_invalidRegex_skipRegex() {
    CsvTagFilter csvTagFilter = (CsvTagFilter) super.filter;
    csvTagFilter.setPath(getClass().getClassLoader().getResource("testData/filter_error_regex.csv").getPath());
    csvTagFilter.init();
    assertEquals(0,csvTagFilter.simpleFilter.size());
    assertEquals(0,csvTagFilter.patterns.size());
  }

  @Test
  void init_invalidPath_skipInit() {
    CsvTagFilter csvTagFilter = (CsvTagFilter) super.filter;
    csvTagFilter.setPath("QIU7");
    csvTagFilter.init();
  }
}
