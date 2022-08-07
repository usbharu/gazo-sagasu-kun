package io.github.usbharu.imagesearch.controller;

import io.github.usbharu.imagesearch.domain.model.ImageTag;
import io.github.usbharu.imagesearch.domain.service.ImageSearch;
import io.github.usbharu.imagesearch.domain.service.TagService;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller

public class IndexController {

  Log log = LogFactory.getLog(IndexController.class);
  @Autowired
  private ImageSearch imageSearch;

  @Autowired
  private TagService tagService;

  @GetMapping("/")
  public String index(Model model) {
    log.trace("Access to Index");
    model.addAttribute("message", imageSearch.randomTag().getName());
    return "index";
  }


  @GetMapping("/searched")
  public String searched(@ModelAttribute("tags") String msg, @ModelAttribute("sort") String sort,
      @ModelAttribute("order") String order, Model model) {

    log.trace("Access to Searched : " + msg);
    List<ImageTag> list = new ArrayList<>(imageSearch.search(msg.split("[; ,]"), sort, order));
    model.addAttribute("tagCount", tagService.tagOrderOfMostUsedLimit(20));
    model.addAttribute("message", msg);
    model.addAttribute("order", order);
    model.addAttribute("sort", sort);
    model.addAttribute("images", list);
    return "searched";
  }
}
