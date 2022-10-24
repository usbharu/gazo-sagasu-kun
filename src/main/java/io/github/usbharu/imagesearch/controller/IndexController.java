package io.github.usbharu.imagesearch.controller;

import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.model.custom.Images;
import io.github.usbharu.imagesearch.domain.service.GroupService;
import io.github.usbharu.imagesearch.domain.service.ImageSearch;
import io.github.usbharu.imagesearch.domain.service.ImageService;
import io.github.usbharu.imagesearch.domain.service.TagService;
import io.github.usbharu.imagesearch.util.ImageFileNameUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller

public class IndexController {

  final BuildProperties buildProperties;
  private final ImageFileNameUtil imageFileNameUtil;
  private final ImageSearch imageSearch;
  private final TagService tagService;
  private final GroupService groupService;
  private final ImageService imageService;
  Logger logger = LoggerFactory.getLogger(IndexController.class);
  @Value("${imagesearch.scan.http.folder:}")
  private String httpFolder = "";
  @Value("${imagesearch.scan.folder:}")
  private String folder = "";

  @Autowired
  public IndexController(ImageSearch imageSearch, TagService tagService, GroupService groupService,
      ImageService imageService, BuildProperties buildProperties,
      ImageFileNameUtil imageFileNameUtil) {
    this.imageSearch = imageSearch;
    this.tagService = tagService;
    this.groupService = groupService;
    this.imageService = imageService;
    this.buildProperties = buildProperties;
    this.imageFileNameUtil = imageFileNameUtil;
  }

  @GetMapping("/")
  public String index(Model model) {
    logger.trace("Access to Index");
    model.addAttribute("message", imageSearch.randomTag().getName());
    model.addAttribute("groups", groupService.getGroupsAndAll());
    model.addAttribute("version", buildProperties.getVersion());
    return "index";
  }


  @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("ICAST_INT_CAST_TO_DOUBLE_PASSED_TO_CEIL")
  @SuppressWarnings("IntegerDivisionInFloatingPointContext")
  @GetMapping("/search")
  public String search(@ModelAttribute("tags") String tags, @ModelAttribute("group") String group,
      @ModelAttribute("sort") String sort, @ModelAttribute("order") String order,
      @ModelAttribute("duplicate") String duplicate, @ModelAttribute("limit") String limitStr,
      @ModelAttribute("page") String pageStr, @ModelAttribute("link") String link,
      @ModelAttribute("merge") String mergeStr, Model model) {
    if (duplicate != null && !duplicate.isBlank()) {
      return "redirect:/image/" + duplicate;
    }
    if (link != null && !link.isBlank()) {
      return "redirect:" + link;
    }

    if (!groupService.validation(group)) {
      group = "";
    }

    int limit = 100;
    if (limitStr != null && !limitStr.isBlank()) {
      try {
        limit = Integer.parseInt(limitStr);
      } catch (NumberFormatException e) {
        logger.warn("Illegal Page Number", e);
      }
    }
    int page = 0;
    if (pageStr != null && !pageStr.isBlank()) {
      try {
        page = Integer.parseInt(pageStr);
      } catch (NumberFormatException e) {
        logger.warn("Illegal Page Number", e);
      }
    }
    boolean merge = false;
    if (mergeStr != null && !mergeStr.isBlank()) {
      merge = Boolean.parseBoolean(mergeStr);
    }

    Images images =
        imageSearch.search3(tags.split("[; ,]"), group, sort, order, limit, page, merge);
    model.addAttribute("tagCount", tagService.tagOrderOfMostUsedLimit(20));
    model.addAttribute("message", tags);
    model.addAttribute("images", images);
    model.addAttribute("httpUrl", httpFolder);
    model.addAttribute("groups", groupService.getGroupsAndAll());
    model.addAttribute("version", buildProperties.getVersion());
    model.addAttribute("limit", limit);
    model.addAttribute("page", page);
    model.addAttribute("pageCount", ((int) Math.ceil(images.getCount() / limit)));
    model.addAttribute("count", images.getCount());
    model.addAttribute("merge", merge);
    return "search";
  }

  @GetMapping("/image/{id}")
  public String image(@PathVariable("id") Integer id, Model model) {
    Image image = imageService.findById(id);
    model.addAttribute("image", image);
    model.addAttribute("httpUrl", httpFolder);
    model.addAttribute("version", buildProperties.getVersion());
    return "image";
  }

  @GetMapping("/image/file")
  public HttpEntity<byte[]> image(@ModelAttribute("path") String path, Model model) {
    File file = new File(folder + File.separator + path);
    byte[] byteImg = null;
    HttpHeaders httpHeaders = null;
    try {
      byteImg = Files.readAllBytes(file.toPath());
      httpHeaders = new HttpHeaders();
      if (imageFileNameUtil.isJpg(file.getName())) {
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
      } else if (imageFileNameUtil.isPng(file.getName())) {
        httpHeaders.setContentLength(byteImg.length);
      }
    } catch (IOException e) {
      return null;
    }
    return new HttpEntity<>(byteImg, httpHeaders);
  }

  @GetMapping("/redirect")
  public String redirectTo(@ModelAttribute("url") String url) {
    return "redirect:" + url;
  }

  @GetMapping("/link")
  public String linkTo(@ModelAttribute("url") String url) {
    return "redirect:" + url;
  }
}
