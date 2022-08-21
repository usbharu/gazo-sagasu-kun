package io.github.usbharu.imagesearch.domain.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import io.github.usbharu.imagesearch.ImageSearchApplication;
import io.github.usbharu.imagesearch.db.test.CsvDataSetLoader;
import io.github.usbharu.imagesearch.domain.model.Image;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;


@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class
})
@SpringBootTest(classes = {
    ImageSearchApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
class ImageDaoTest {

  @Autowired
  private ImageDao imageDao;

  @Test
  @DatabaseSetup(value = "/imageDB/")
  void findAll_findAll_returnAllImages() {
    List<Image> all = imageDao.findAll();
    System.out.println("all.size() = " + all.size());
    assertEquals(300, all.size());

  }

  @Test
  @DatabaseSetup(value = "/imageDB/")
  void findByName_findByName_returnImage() {
    List<Image> images = imageDao.findByName("1.jpg");
    for (Image image : images) {
      assertEquals("1.jpg", image.getName());
    }
    assertEquals(3,images.size());
  }


  @Test
  @DatabaseSetup(value = "/imageDB/")
  void findByUrl_findByUrl_returnImage() {
    Image image = imageDao.findByUrl("/testData/1/1.jpg");
    assertEquals("1.jpg", image.getName());
  }

  @Test
  @DatabaseSetup(value = "/imageDB/forInsert/")
  @ExpectedDatabase(value = "/imageDB/forInsert/expected/", assertionMode = DatabaseAssertionMode.NON_STRICT)
  void insertOne_insertOne_returnOne() {
    Image image = new Image("new.jpg", "/testData/1/new.jpg", 1);
    System.out.println("imageDao.findAll().size() = " + imageDao.findAll().size());
    int result = imageDao.insertOne(image);
    System.out.println("imageDao.findAll().size() = " + imageDao.findAll().size());
    assertEquals(1, result);
  }

  @Test
  @DatabaseSetup(value = "/imageDB/")
  void selectOne_selectOne_returnImage() {
    Image image = imageDao.selectOne("/testData/1/1.jpg");
    assertEquals("1.jpg", image.getName());
  }

  @Test
  @DatabaseSetup(value = "/imageDB/forDelete/")
  @ExpectedDatabase(value = "/imageDB/forDelete/expected/", assertionMode = DatabaseAssertionMode.NON_STRICT)
  void deleteOne_deleteOne_returnOne() {
    Image image = new Image("delete.jpg", "/testData/1/delete.jpg", 1);
    int result = imageDao.deleteOne(image.getPath());
    assertEquals(1, result);
  }
}
