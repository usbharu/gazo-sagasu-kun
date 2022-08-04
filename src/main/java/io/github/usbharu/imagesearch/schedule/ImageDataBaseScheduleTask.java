package io.github.usbharu.imagesearch.schedule;

import io.github.usbharu.imagesearch.domain.service.ImageScan;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ImageDataBaseScheduleTask {

  @Autowired private ImageScan imageScan;

  Log log = LogFactory.getLog(ImageDataBaseScheduleTask.class);

  @Scheduled(cron = "0 0 * * * *")
  public void scan() throws UnknownHostException {
    log.info("Start Scheduled Scan");
    imageScan.scan(Paths.get("/mnt/資料/趣味/クッキークリッカー/しょぼいマフィンベーカリー/"));
    log.info("End Scheduled Scan");
  }
}
