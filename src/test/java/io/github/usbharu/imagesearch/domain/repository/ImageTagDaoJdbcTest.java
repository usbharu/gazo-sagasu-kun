package io.github.usbharu.imagesearch.domain.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import io.github.usbharu.imagesearch.ImageSearchApplication;
import io.github.usbharu.imagesearch.db.test.CsvDataSetLoader;
import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.model.ImageTag;
import io.github.usbharu.imagesearch.domain.model.Tag;
import io.github.usbharu.imagesearch.util.ImageFileNameUtil;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@SpringBootTest(classes = {
    ImageSearchApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
class ImageTagDaoJdbcTest {
  @Autowired private ImageTagDaoJdbc imageTagDao;


  @Test
  @DatabaseSetup(value = "/image_tagDB/")
  void findAll_findAll_returnAllImageTags() {
    List<ImageTag> all = imageTagDao.findAll();
    int count = 0;
    for (ImageTag imageTag : all) {
      count += imageTag.getTags().size();
    }
    assertEquals(250, count);
  }

  @Test
  @DatabaseSetup(value = "/image_tagDB/")
  void findByImageUrl_findByImageUrl_returnImageTag() throws UnknownHostException {
    String url = ImageFileNameUtil.getURL(Paths.get("/testData/1/1.jpg").toAbsolutePath().toFile());
    System.out.println("url = " + url);
    String url1 = url.replaceAll("\\\\", "/");
    ImageTag imageTag = imageTagDao.findByImageUrl(url1);
    assertEquals(url1, imageTag.getImage().getPath());
    assertEquals(1, imageTag.getTags().size());
  }

  @Test
  @DatabaseSetup(value = "/image_tagDB/")
  void findByTagId_findByTagId_returnAllImageTag() {
    List<ImageTag> imageTags = imageTagDao.findByTagId(1);
    assertEquals(33, imageTags.size());
  }

  @Test
  @DatabaseSetup(value = "/image_tagDB/")
  void findByTagName_findByTagName_returnAllImageTag() {
    List<ImageTag> imageTags = imageTagDao.findByTagName("tag1");
    assertEquals(33, imageTags.size());
  }

  @Test
  @DatabaseSetup(value = "/image_tagDB/")
  void findByTagIds_findByTagIds_returnAllImageTag() {
    List<ImageTag> imageTags = imageTagDao.findByTagIds(List.of(1, 4));
    assertEquals(3, imageTags.size());
  }

  @Test
  @DatabaseSetup(value = "/image_tagDB/")
  void findByTagNames_findByTagNames_returnAllImageTag() {
    List<ImageTag> imageTags = imageTagDao.findByTagNames(List.of("tag1", "tag4"));
    assertEquals(3, imageTags.size());
  }

  @Test
  @DatabaseSetup(value = "/image_tagDB/forInsert/")
  void insertOne_insertOne_returnOne() {
    int i = imageTagDao.insertOne(new ImageTag(new Image(2, "1.jpg",
        ImageFileNameUtil.filePath + "\\testData\\1\\1.jpg",
        1), List.of(new Tag(1, "tag1"))));
    assertEquals(1,i);
  }
}
