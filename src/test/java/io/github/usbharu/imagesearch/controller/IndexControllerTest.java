package io.github.usbharu.imagesearch.controller;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import io.github.usbharu.imagesearch.domain.model.Group;
import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.model.Tag;
import io.github.usbharu.imagesearch.domain.model.custom.Images;
import io.github.usbharu.imagesearch.domain.model.custom.TagCount;
import io.github.usbharu.imagesearch.domain.service.GroupService;
import io.github.usbharu.imagesearch.domain.service.ImageSearch;
import io.github.usbharu.imagesearch.domain.service.ImageService;
import io.github.usbharu.imagesearch.domain.service.TagService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;


@SpringBootTest
@AutoConfigureMockMvc
class IndexControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  ImageService imageService;

  @MockBean
  TagService tagService;


  @MockBean
  ImageSearch imageSearch;

  @MockBean
  GroupService groupService;

  @InjectMocks
  IndexController indexController;

  @Autowired
  WebApplicationContext webApplicationContext;

  @Test
  void index_get_return200() throws Exception {

    when(imageSearch.randomTag()).thenReturn(new Tag(1, "test"));

    this.mockMvc.perform(get("/"))
        .andDo(print())
        .andExpect(model().attribute("message","test"))
        .andExpect(status().isOk());
  }

  // TODO: 2022/09/08 テストが甘いので追加する
  @Test
  void search_getWithTags_returnImages() throws Exception {

    Images images = new Images(3);
    List<Image> es =
        List.of(new Image("0.jpg", "testData/1/0.jpg"), new Image("1.jpg", "testData/1/1.jpg"),
            new Image("2.jpg", "testData/1/2.jpg"));
    images.addAll(es);
    when(imageSearch.search3(any(String[].class),any(),any(),any(),anyInt(),anyInt(),eq(false))).thenReturn(images);

    when(tagService.tagOrderOfMostUsedLimit(anyInt())).thenReturn(List.of(
        new TagCount(37, new Tag(10, "tag8")),
        new TagCount(33, new Tag(1, "tag1")),
        new TagCount(28, new Tag(5, "tag2")),
        new TagCount(23, new Tag(11, "tag7")),
        new TagCount(22, new Tag(6, "tag11")),
        new TagCount(22, new Tag(4, "tag4")),
        new TagCount(19, new Tag(9, "tag9")),
        new TagCount(19, new Tag(8, "tag3")),
        new TagCount(18, new Tag(2, "tag6")),
        new TagCount(17, new Tag(3, "tag5")),
        new TagCount(12, new Tag(7, "tag10"))
    ));

    when(groupService.getGroupsAndAll()).thenReturn(List.of(new Group(1, "a"),
        new Group(2, "b"),
        new Group(3, "c"),
        new Group("all")));

    this.mockMvc.perform(get("/search").param("tags", "tag1"))
        .andDo(print())
        .andExpect(model().attribute("images", hasItem(hasProperty("name", is("0.jpg")))))
        .andExpect(view().name("search")).andExpect(status().isOk());
  }

  @Test
  void search_lotsOfResults_returnPagedImages() throws Exception {
    Images images = new Images(102);

    for (int i = 0; i < 102; i++) {
      images.add(new Image("image"+i+".jpg","image"+i+".jpg"));
    }
    when(imageSearch.search3(any(String[].class),any(),any(),any(),anyInt(),anyInt(),eq(false))).thenReturn(images);

    when(tagService.tagOrderOfMostUsedLimit(anyInt())).thenReturn(List.of(
        new TagCount(37, new Tag(10, "tag8")),
        new TagCount(33, new Tag(1, "tag1")),
        new TagCount(28, new Tag(5, "tag2")),
        new TagCount(23, new Tag(11, "tag7")),
        new TagCount(22, new Tag(6, "tag11")),
        new TagCount(22, new Tag(4, "tag4")),
        new TagCount(19, new Tag(9, "tag9")),
        new TagCount(19, new Tag(8, "tag3")),
        new TagCount(18, new Tag(2, "tag6")),
        new TagCount(17, new Tag(3, "tag5")),
        new TagCount(12, new Tag(7, "tag10"))
    ));


    when(groupService.getGroupsAndAll()).thenReturn(List.of(new Group(1, "a"),
        new Group(2, "b"),
        new Group(3, "c"),
        new Group("all")));

    this.mockMvc.perform(get("/search").param("tags","tag1").param("limit","10")).andDo(print()).andExpect(status().isOk()).andExpect(model().attribute("pageCount",10));
  }

  @Test
  void image_getImage_returnImage() throws Exception {

    when(imageService.findById(anyInt())).thenReturn(new Image("0.jpg", "testData/1/0.jpg"));

    this.mockMvc.perform(get("/image/1/")).andDo(print())
        .andExpect(model().attribute("image", hasProperty("name", is("0.jpg"))))
        .andExpect(view().name("image")).andExpect(status().isOk());
  }
}
