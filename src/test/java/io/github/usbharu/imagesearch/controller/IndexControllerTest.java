package io.github.usbharu.imagesearch.controller;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import io.github.usbharu.imagesearch.ImageSearchApplication;
import io.github.usbharu.imagesearch.db.test.CsvDataSetLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;


@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
@AutoConfigureDataJdbc
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@AutoConfigureMockMvc
@SpringBootTest(classes = ImageSearchApplication.class)
class IndexControllerTest {

  @Autowired
  private MockMvc mockMvc;
//
//  @Mock
//  private ImageSearch imageSearch;
//
//  @Mock private TagService tagService;

  //  @InjectMocks private IndexController indexController;
  private AutoCloseable autoCloseable;

  @Autowired
  IndexController indexController;

  @BeforeEach
  void setUp() {
//    autoCloseable = MockitoAnnotations.openMocks(this);
//    this.mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();
  }

  @AfterEach
  void tearDown() throws Exception {
//    autoCloseable.close();
  }


  @DatabaseSetup(value = "/tagDB/")
  @Test
  void index_get_return200() throws Exception {
    this.mockMvc.perform(get("/")).andExpect(model().attributeExists("message")).andDo(print())
        .andExpect(status().isOk());
  }

  // TODO: 2022/09/08 テストが甘いので追加する
  @Test
  @DatabaseSetup(value = "/indexControllerDB/")
  void search_getWithTags_returnImages() throws Exception {
    this.mockMvc.perform(get("/search").param("tags", "tag1"))
        .andExpect(model().attribute("images", hasItem(hasProperty("name", is("0.jpg")))))
        .andExpect(view().name("search")).andExpect(status().isOk());
  }

  @Test
  @DatabaseSetup(value = "/imageDB/")
  void image_getImage_returnImage() throws Exception {
    this.mockMvc.perform(get("/image/1/")).andDo(print()).andExpect(model().attribute("image", hasProperty("name", is("0.jpg"))))
        .andExpect(view().name("image")).andExpect(status().isOk());
  }
}
