package io.github.usbharu.imagesearch.schedule;

import io.github.usbharu.imagesearch.domain.service.ImageScanner;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ImageDataBaseScheduleTask {

  @Autowired
  ImageScanner imageScanner;

  Logger logger = LoggerFactory.getLogger(ImageDataBaseScheduleTask.class);

  @Scheduled(cron = "0 0 * * * *")
  public void scan() {
    logger.info("Start Scheduled Scan");
    imageScanner.startScan();
    logger.info("End Scheduled Scan");
  }
}
