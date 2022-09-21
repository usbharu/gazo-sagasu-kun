package io.github.usbharu.imagesearch.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.model.Tag;
import io.github.usbharu.imagesearch.domain.model.Tags;
import io.github.usbharu.imagesearch.domain.model.custom.Images;
import io.github.usbharu.imagesearch.domain.repository.TagDao;
import io.github.usbharu.imagesearch.domain.repository.custom.DynamicSearchDao;
import io.github.usbharu.imagesearch.domain.service.duplicate.DuplicateCheck;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


class ImageSearchTest {

  @Mock
  DynamicSearchDao dynamicSearchDao;

  @Mock
  TagDao tagDao;

  @Mock
  DuplicateCheck duplicateCheck;

  @InjectMocks ImageSearch imageSearch;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void mergeSequentialNumbers_SequentialImages_mergedImages() {
    Images images = new Images(4);
    Tags tags = new Tags();
    tags.add(new Tag("--123456--"));
    Image image1 = new Image("1.jpg","1.jpg");
    Image image2 = new Image("2.jhg","2.jpg");
    Image image3 = new Image("3.jpg","3.jpg");

    Image image4 = new Image("4.jpg","4.jpg");
    image1.addMetadata(tags);
    image2.addMetadata(tags);
    image3.addMetadata(tags);

    Tags tags2 = new Tags();
    tags2.add(new Tag("--123457--"));
    image4.addMetadata(tags2);
    images.addAll(List.of(image1,image2,image3,image4));
    Images result = imageSearch.mergeSequentialNumbers(images);
    assertEquals(2,result.size());
  }
}