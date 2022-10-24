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
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
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
    assertEquals(3, images.size());
  }

  @Test
  void findByName_NameIsNull_throwNullPointException() {
    assertThrows(NullPointerException.class, () -> imageDao.findByName(null));
  }

  @Test
  void findByName_NameIsEmpty_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> imageDao.findByName(""));
  }

  @Test
  void findByName_NameIsBlank_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> imageDao.findByName(" "));
  }

  @Test
  @DatabaseSetup(value = "/imageDB/")
  void findByUrl_findByUrl_returnImage() {
    Image image = imageDao.findByUrl("/testData/1/1.jpg");
    assertEquals("1.jpg", image.getName());
  }

  @Test
  @DatabaseSetup(value = "/imageDB/")
  void findByUrl_findByIllegalUrl_returnNull() {
    assertNull(imageDao.findByUrl("xyk6i0g"));
  }

  @Test
  void findByUrl_UrlIsNull_throwNullPointException() {
    assertThrows(NullPointerException.class, () -> imageDao.findByUrl(null));
  }

  @Test
  void findByUrl_UrlIsEmpty_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> imageDao.findByUrl(""));
  }

  @Test
  void findByUrl_UrlIsBlank_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> imageDao.findByUrl(" "));
  }

  @Test
  void findById_findByNegative_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> imageDao.findById(-1));
  }

  @Test
  void findById_findByZero_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> imageDao.findById(0));
  }

  @Test
  @DatabaseSetup(value = "/imageDB/")
  void findById_findByIllegalId_returnNull() {
    assertNull(imageDao.findById(472894));
  }

  // TODO: 2022/09/07 IDが指定されたとき、IDは挿入されていないことを確認するテストケースを作る
  @Test
  @DatabaseSetup(value = "/imageDB/forInsert/")
  @ExpectedDatabase(value = "/imageDB/forInsert/expected/", assertionMode = DatabaseAssertionMode.NON_STRICT)
  void insertOne_insertOne_returnOne() {
    Image image = new Image("new.jpg", "/testData/1/new.jpg", 1);
    int result = imageDao.insertOne(image);
    assertEquals(1, result);
  }

  @Test
  void insertOne_insertNull_throwNullPointException() {
    assertThrows(NullPointerException.class, () -> imageDao.insertOne(null));
  }

  @Test
  @DatabaseSetup(value = "/imageDB/forInsertWithReturn/")
  @ExpectedDatabase(value = "/imageDB/forInsertWithReturn/expected/", assertionMode = DatabaseAssertionMode.NON_STRICT)
  void insertOneWithReturnImage_insertOne_returnInsertedImage() {
    Image image = new Image("newWithReturn.jpg", "/testData/1/newWithReturn.jpg", 1);
    Image image1 = imageDao.insertOneWithReturnImage(image);

    assertEquals(image.getGroup(), image1.getGroup());
    assertEquals(image.getName(), image1.getName());
    assertEquals(image.getPath(), image1.getPath());
  }

  @Test
  void insertOneWithReturnImage_insertNull_throwNullPointException() {
    assertThrows(NullPointerException.class, () -> imageDao.insertOneWithReturnImage(null));
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

  @Test
  void deleteOne_deleteNull_throwNullPointException() {
    assertThrows(NullPointerException.class, () -> imageDao.deleteOne(null));
  }

  @Test
  void deleteOne_deleteEmpty_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> imageDao.deleteOne(""));
  }

  @Test
  void deleteOne_deleteBlank_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> imageDao.deleteOne(" "));
  }

  @Test
  @DatabaseSetup(value = "/imageDB/")
  void deleteOne_deleteIllegalUrl_returnZero() {
    int i = imageDao.deleteOne("d01cab59-0e18-4ebd-a1e6-a4ad5626c361");
    assertEquals(0, i);
  }
}
