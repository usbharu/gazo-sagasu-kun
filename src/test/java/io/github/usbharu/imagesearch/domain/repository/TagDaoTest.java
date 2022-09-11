package io.github.usbharu.imagesearch.domain.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import io.github.usbharu.imagesearch.ImageSearchApplication;
import io.github.usbharu.imagesearch.db.test.CsvDataSetLoader;
import io.github.usbharu.imagesearch.domain.model.Tag;
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
class TagDaoTest {

  @Autowired
  private TagDao tagDao;

  @Test
  @DatabaseSetup(value = "/tagDB/")
  void findAll_findAll_returnAllTags() {
    List<Tag> all = tagDao.findAll();
    System.out.println("all.size() = " + all.size());
    assertEquals(11, all.size());
  }

  @Test
  @DatabaseSetup(value = "/tagDB/")
  void findById_findById_returnTag() {
    Tag tag = tagDao.findById(1);
    assertEquals("tag1", tag.getName());
  }

  @Test
  void findById_findByNegative_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> tagDao.findById(-1));
  }

  @Test
  void findById_findByZero_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> tagDao.findById(0));
  }

  @Test
  @DatabaseSetup(value = "/tagDB/")
  void findByName_findByName_returnTag() {
    List<Tag> tags = tagDao.findByName("tag1");
    assertEquals(1, tags.size());
  }

  @Test
  void findByName_findByNull_throwNullPointException() {
    assertThrows(NullPointerException.class, () -> tagDao.findByName(null));
  }

  @Test
  void findByName_findByEmpty_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> tagDao.findByName(""));
  }

  @Test
  void findByName_findByBlank_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> tagDao.findByName(" "));
  }

  @Test
  @DatabaseSetup(value = "/tagDB/forInsert/")
  void insert_insert_returnOne() {
    assertEquals(1, tagDao.insertOne("newTag"));
  }

  @Test
  void insertOne_insertNull_throwNullPointException() {
    assertThrows(NullPointerException.class, () -> tagDao.insertOne(null));
  }

  @Test
  void insertOne_insertEmpty_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> tagDao.insertOne(""));
  }

  @Test
  void insertOne_insertBlank_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> tagDao.insertOne(" "));
  }

  @Test
  @DatabaseSetup(value = "/tagDB/forInsertOneWithReturnTag/")
  void insertOneWithReturnTag_insertOneWithReturnTag_returnTag() {
    Tag tag = tagDao.insertOneWithReturnTag("newTag");
    assertEquals("newTag", tag.getName());
  }

  @Test
  void insertOneWithReturnTag_insertNull_throwNullPointException() {
    assertThrows(NullPointerException.class, () -> tagDao.insertOneWithReturnTag(null));
  }

  @Test
  void insertOneWithReturnTag_insertEmpty_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> tagDao.insertOneWithReturnTag(""));
  }

  @Test
  void insertOneWithReturnTag_insertBlank_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> tagDao.insertOneWithReturnTag(" "));
  }

  @Test
  @DatabaseSetup(value = "/tagDB/forDelete/")
  void delete_delete_returnOne() {
    assertEquals(1, tagDao.deleteOne(new Tag(1, "tag1")));
  }

  @Test
  void deleteOne_deleteNull_throwNullPointException() {
    assertThrows(NullPointerException.class, () -> tagDao.deleteOne(null));
  }

  // TODO: 2022/09/07 expectedも追加する
  @Test
  @DatabaseSetup(value = "/tagDB/forDelete/")
  void deleteOne_deleteIllegalTag_returnZero() {
    assertEquals(0, tagDao.deleteOne(new Tag(3450818, "L4gxWG")));
  }

  @Test
  @DatabaseSetup(value = "/tagDB/forDeleteById/")
  void deleteById_deleteById_returnOne() {
    assertEquals(1, tagDao.deleteById(1));
  }

  @Test
  void deleteById_deleteByNegative_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> tagDao.deleteById(-1));
  }

  @Test
  @DatabaseSetup(value = "/tagDB/forDelete/")
  void deleteById_deleteByIllegalId_returnZero() {
    assertEquals(0, tagDao.deleteById(3450818));
  }

  @Test
  void deleteById_deleteByZero_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> tagDao.deleteById(0));
  }

  @Test
  @DatabaseSetup(value = "/tagDB/forDeleteByName/")
  void deleteByName_deleteByName_returnOne() {
    assertEquals(1, tagDao.deleteByName("tag1"));
  }

  @Test
  void deleteByName_deleteByNull_throwNullPointException() {
    assertThrows(NullPointerException.class, () -> tagDao.deleteByName(null));
  }

  @Test
  void deleteByName_deleteByEmpty_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> tagDao.deleteByName(""));
  }

  @Test
  void deleteByName_deleteByBlank_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> tagDao.deleteByName(" "));
  }

  @Test
  void deleteByName_deleteByIllegalName_returnZero() {
    assertEquals(0, tagDao.deleteByName("Q3wJN"));
  }
}
