package io.github.usbharu.imagesearch.controller;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import io.github.usbharu.imagesearch.ImageSearchApplication;
import io.github.usbharu.imagesearch.db.test.CsvDataSetLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
@AutoConfigureDataJdbc
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class
})
//@AutoConfigureMockMvc
@SpringBootTest(classes = ImageSearchApplication.class)
class IndexControllerTest {


  private MockMvc mockMvc;
//
//  @Mock
//  private ImageSearch imageSearch;
//
//  @Mock private TagService tagService;

//  @InjectMocks private IndexController indexController;
  private AutoCloseable autoCloseable;

@Autowired IndexController indexController;
  @BeforeEach
  void setUp() {
    autoCloseable = MockitoAnnotations.openMocks(this);
    this.mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();
  }

  @AfterEach
  void tearDown() throws Exception {
    autoCloseable.close();
  }


  @DatabaseSetup(value = "/tagDB/")
  @Test
  void index_get_return200() throws Exception {
    this.mockMvc.perform(get("/")).andExpect(model().attributeExists("message")).andDo(print()).andExpect(status().isOk());
  }
}
