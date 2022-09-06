package io.github.usbharu.imagesearch;

import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.service.ImageScanner;
//import io.github.usbharu.imagesearch.image.duplicate.DuplicateCheck;
import io.github.usbharu.imagesearch.image.duplicate.DuplicateCheck;
import java.io.File;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableScheduling
public class ImageSearchApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext ctx = SpringApplication.run(ImageSearchApplication.class, args);
    ImageScanner scanner = ctx.getBean(ImageScanner.class);
    scanner.startScan();
    DuplicateCheck duplicateCheck = ctx.getBean(DuplicateCheck.class);
    duplicateCheck.addAllImage();
//    System.out.println("Check all");
//    extracted(duplicateCheck.checkAll());
//    System.out.println("Check all2");
//    extracted(duplicateCheck.checkAll2());
  }

  private static void extracted( List<List<Image>> lists) {
    for (List<Image> list : lists) {
      StringBuilder sb = new StringBuilder();
      for (Image image : list) {
        sb.append(image.getId()).append(" , ");
      }
      System.out.println(sb);
    }
  }

}
