package io.github.usbharu.imagesearch.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SettingController {

  @Value("${server.port}")
  String serverPort;

  @Value("${imagesearch.scan.folder}")
  String imageSearchScanFolder;

  @Value("${imagesearch.scan.depth}")
  String imagesearchScanDepth;

  @Value("${imagesearch.scan.http.folder}")
  String imagesearchScanHttpFolder;

  @Value("${imagesearch.scan.impl}")
  String imagesearchScanImpl;

  @Value("${imagesearch.scan.filter.path}")
  String imagesearchScanFilterPath;

  @Value("${imagesearch.scan.unifier.path}")
  String imagesearchScanUnifierPath;

  @GetMapping("/setting")
  public String setting(Model model) {
    model.addAttribute("settings",    Map.of("server.port", serverPort, "imagesearch.scan.folder", imageSearchScanFolder,
        "imagesearch.scan.depth", imagesearchScanDepth, "imagesearch.scan.http.folder", imagesearchScanHttpFolder,
        "imagesearch.scan.impl", imagesearchScanImpl, "imagesearch.scan.filter.csv",
        imagesearchScanFilterPath, "imagesearch.scan.unifier.csv", imagesearchScanUnifierPath));
    return "setting";
  }
}
