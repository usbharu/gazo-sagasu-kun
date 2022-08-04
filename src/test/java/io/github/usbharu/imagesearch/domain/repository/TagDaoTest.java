package io.github.usbharu.imagesearch.domain.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import io.github.usbharu.imagesearch.ImageSearchApplication;
import io.github.usbharu.imagesearch.db.test.CsvDataSetLoader;
import io.github.usbharu.imagesearch.domain.model.Tag;
import java.util.List;
import org.junit.jupiter.api.Assertions;
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
@SpringBootTest(classes = {ImageSearchApplication.class},webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
class TagDaoTest {

  @Autowired private TagDao tagDao;

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
  @DatabaseSetup(value = "/tagDB/")
  void findByName_findByName_returnTag() {
    List<Tag> tags = tagDao.findByName("tag1");
    assertEquals(1, tags.size());
  }

  @Test
  @DatabaseSetup(value = "/tagDB/forInsert/")
  void insert_insert_returnOne(){
    assertEquals(1, tagDao.insertOne("newTag"));
  }

  @Test
  @DatabaseSetup(value = "/tagDB/forInsertOneWithReturnTag/")
  void insertOneWithReturnTag_insertOneWithReturnTag_returnTag(){
    Tag tag = tagDao.insertOneWithReturnTag("newTag");
    assertEquals("newTag", tag.getName());
  }

  @Test
  @DatabaseSetup(value = "/tagDB/forDelete/")
  void delete_delete_returnOne(){
    assertEquals(1, tagDao.deleteOne(new Tag(1, "tag1")));
  }

  @Test
  @DatabaseSetup(value = "/tagDB/forDeleteById/")
  void deleteById_deleteById_returnOne(){
    assertEquals(1, tagDao.deleteById(1));
  }

  @Test
  @DatabaseSetup(value = "/tagDB/forDeleteByName/")
  void deleteByName_deleteByName_returnOne(){
    assertEquals(1, tagDao.deleteByName("tag1"));
  }
}
