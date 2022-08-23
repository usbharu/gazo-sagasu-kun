package io.github.usbharu.imagesearch.controller;

import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.service.GroupService;
import io.github.usbharu.imagesearch.domain.service.ImageSearch;
import io.github.usbharu.imagesearch.domain.service.ImageService;
import io.github.usbharu.imagesearch.domain.service.TagService;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller

public class IndexController {

  Logger logger = LoggerFactory.getLogger(IndexController.class);

  private final ImageSearch imageSearch;


  private final TagService tagService;

  private final GroupService groupService;

  private final ImageService imageService;

  @Autowired
  public IndexController(ImageSearch imageSearch, TagService tagService,
      GroupService groupService, ImageService imageService) {
    this.imageSearch = imageSearch;
    this.tagService = tagService;
    this.groupService = groupService;
    this.imageService = imageService;
  }

  @Value("${imagesearch.scan.http.folder:}")
  private String httpFolder = "";

  @GetMapping("/")
  public String index(Model model) {
    logger.trace("Access to Index");
    model.addAttribute("message", imageSearch.randomTag().getName());
    model.addAttribute("groups", groupService.getGroupsAndAll());
    return "index";
  }

//  @GetMapping("/searched")
//  public String searched(@ModelAttribute("tags") String msg, @ModelAttribute("sort") String sort,
//      @ModelAttribute("order") String order, Model model) {
//
//    System.out.println("msg = " + msg);
//    log.trace("Access to Searched : " + msg);
//    List<Image> list = new ArrayList<>(imageSearch.search2(msg.split("[; ,]"), sort, order));
//    model.addAttribute("tagCount", tagService.tagOrderOfMostUsedLimit(20));
//    model.addAttribute("message", msg);
//    model.addAttribute("order", order);
//    model.addAttribute("sort", sort);
//    model.addAttribute("images", list);
//    model.addAttribute("httpUrl", httpFolder);
//    return "searched";
//  }

  @GetMapping("/search")
  public String search(@ModelAttribute("tags") String tags,
      @ModelAttribute("group") String group,
      @ModelAttribute("sort") String sort,
      @ModelAttribute("order") String order,
      Model model) {
    List<Image> images = imageSearch.search3(tags.split("[; ,]"), group, sort, order);
    model.addAttribute("tagCount", tagService.tagOrderOfMostUsedLimit(20));
    model.addAttribute("message", tags);
    model.addAttribute("images", images);
    model.addAttribute("httpUrl", httpFolder);
    model.addAttribute("groups", groupService.getGroupsAndAll());
    return "search";
  }

  @GetMapping("/image/{id}")
  public String image(@PathVariable("id") Integer id, Model model) {
    Image image = imageService.findById(id);
    model.addAttribute("image", image);
    model.addAttribute("httpUrl",httpFolder);
    return "image";
  }
}
