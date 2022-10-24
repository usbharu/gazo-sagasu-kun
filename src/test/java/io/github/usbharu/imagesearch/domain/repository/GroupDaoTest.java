package io.github.usbharu.imagesearch.domain.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import io.github.usbharu.imagesearch.ImageSearchApplication;
import io.github.usbharu.imagesearch.db.test.CsvDataSetLoader;
import io.github.usbharu.imagesearch.domain.model.Group;
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
class GroupDaoTest {

  @Autowired
  GroupDao groupDao;

  @Test
  @DatabaseSetup(value = "/groupDB/")
  void findAll_findAll_returnAllGroup() {
    List<Group> all = groupDao.findAll();
    assertEquals(3, all.size());
  }

  @Test
  @DatabaseSetup(value = "/groupDB/")
  void findById_findById_returnGroup() {
    Group byId = groupDao.findById(1);
    assertEquals(1, byId.getId());
  }

  @Test
  void findById_findByNegative_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> groupDao.findById(-1));
  }

  @Test
  void findById_findByZero_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> groupDao.findById(0));
  }

  @Test
  @DatabaseSetup(value = "/groupDB/")
  void findById_findByIllegalId_returnNull() {
    Group byId = groupDao.findById(100);
    assertNull(byId);
  }

  @Test
  @DatabaseSetup(value = "/groupDB/")
  void findByName_findByName_returnGroup() {
    Group upright = groupDao.findByName("upright");
    assertEquals("upright", upright.getName());
  }

  @Test
  void findByName_findByNull_throwNullPointException() {
    assertThrows(NullPointerException.class, () -> groupDao.findByName(null));
  }

  @Test
  void findByName_findByEmpty_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> groupDao.findByName(""));
  }

  @Test
  void findByName_findByBlank_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> groupDao.findByName(" "));
  }

  @Test
  @DatabaseSetup(value = "/groupDB/")
  void findByName_findByIllegalName_returnNull() {
    Group byName = groupDao.findByName("illegal");
    assertNull(byName);
  }

  @Test
  @DatabaseSetup(value = "/groupDB/forInsert/")
  @ExpectedDatabase(value = "/groupDB/forInsert/expected/", assertionMode = DatabaseAssertionMode.NON_STRICT)
  void insertOne_insertOne_insertOne() {
    int insert = groupDao.insertOne("insert");
    assertEquals(1, insert);
  }

  @Test
  @DatabaseSetup(value = "/groupDB/forInsertWithReturn/")
  @ExpectedDatabase(value = "/groupDB/forInsertWithReturn/expected/", assertionMode = DatabaseAssertionMode.NON_STRICT)
  void insertOneWithReturnGroup_insertOne_returnInsertedGroup() {
    Group insert = groupDao.insertOneWithReturnGroup("insertWithReturn");
    assertEquals("insertWithReturn", insert.getName());
  }

  @Test
  @DatabaseSetup(value = "/groupDB/forDeleteById/")
  @ExpectedDatabase(value = "/groupDB/forDeleteById/expected/", assertionMode = DatabaseAssertionMode.NON_STRICT)
  void deleteOneById_deleteOne_deleteOne() {
    int i = groupDao.deleteOne(1);
    assertEquals(1, i);
  }

  @Test
  void deleteOneById_deleteByNegative_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> groupDao.deleteOne(-1));
  }

  @Test
  void deleteOneById_deleteByZero_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> groupDao.deleteOne(0));
  }

  @Test
  @DatabaseSetup(value = "/groupDB/")
  void deleteOneById_deleteByIllegalId_returnZero() {
    int i = groupDao.deleteOne(100);
    assertEquals(0, i);
  }

  @Test
  @DatabaseSetup(value = "/groupDB/forDeleteByName/")
  @ExpectedDatabase(value = "/groupDB/forDeleteByName/expected/", assertionMode = DatabaseAssertionMode.NON_STRICT)
  void deleteOneByName_deleteOne_deleteOne() {
    int i = groupDao.deleteOne("upright");
    assertEquals(1, i);
  }

  @Test
  void deleteOneByName_deleteByNull_throwNullPointException() {
    assertThrows(NullPointerException.class, () -> groupDao.deleteOne((String) null));
  }

  @Test
  void deleteOneByName_deleteByEmpty_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> groupDao.deleteOne(""));
  }

  @Test
  void deleteOneByName_deleteByBlank_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> groupDao.deleteOne(" "));
  }

  @Test
  @DatabaseSetup(value = "/groupDB/")
  void deleteOneByName_deleteByIllegalName_returnZero() {
    int i = groupDao.deleteOne("illegal");
    assertEquals(0, i);
  }
}
