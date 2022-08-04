package io.github.usbharu.imagesearch;

import io.github.usbharu.imagesearch.domain.service.ImageScan;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableAsync
@SpringBootApplication
@EnableScheduling
public class ImageSearchApplication {

  @Value("${imagesearch.imageFolder}") private String imageFolder;

  @Autowired ImageScan imageScan;

  public static void main(String[] args) {
    ConfigurableApplicationContext ctx = SpringApplication.run(ImageSearchApplication.class, args);
    ImageSearchApplication app = ctx.getBean(ImageSearchApplication.class);
    app.firstScan();
  }

  public void firstScan(){
    imageScan.scan(Paths.get(imageFolder));
//    imageScan.scan(Paths.get("K:\\Documents\\GitHub\\ImageSearch\\src\\test\\resources\\testData"));
//    imageScan.scan(Paths.get("Y:\\資料\\趣味\\クッキークリッカー\\しょぼいマフィンベーカリー"));
//    imageScan.scan(Paths.get("/mnt/資料/趣味/クッキークリッカー/しょぼいマフィンベーカリー/"));
  }

}
