package io.github.usbharu.imagesearch;

import io.github.usbharu.imagesearch.domain.service.ImageScanner;
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
  }

}
