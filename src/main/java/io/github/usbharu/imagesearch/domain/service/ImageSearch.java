package io.github.usbharu.imagesearch.domain.service;

import io.github.usbharu.imagesearch.domain.model.ImageTag;
import io.github.usbharu.imagesearch.domain.repository.ImageTagDaoJdbc;
import io.github.usbharu.imagesearch.domain.repository.ImageTagDaoOrder;
import io.github.usbharu.imagesearch.domain.repository.ImageTagDaoOrderType;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageSearch {

  Log log = LogFactory.getLog(ImageSearch.class);

  @Autowired
  private ImageTagDaoJdbc imageTagDaoJdbc;

  public List<ImageTag> search(String tags){
    return search(tags.split("[, ;]"));
  }

  public List<ImageTag> search(String[] tags){
    log.debug("search tags:" + Arrays.toString(tags));
    List<ImageTag> byTagNames = imageTagDaoJdbc.findByTagNames(List.of(tags));
    log.debug("Success to search : " + byTagNames.size());
    return byTagNames;
  }

  public List<ImageTag> search(String[] tags,String sort,String order){
    log.debug("search tags:" + Arrays.toString(tags)+ " sort:" + sort + " order:" + order);
    ImageTagDaoOrder imageTagDaoOrder;
    ImageTagDaoOrderType imageTagDaoOrderType;
    try {
      imageTagDaoOrder = ImageTagDaoOrder.fromString(order);
      imageTagDaoOrderType = ImageTagDaoOrderType.fromString(sort);
    }catch (IllegalArgumentException e){
      imageTagDaoOrderType = ImageTagDaoOrderType.IMAGE_ID;
      imageTagDaoOrder = ImageTagDaoOrder.ASC;
    }
    List<ImageTag> byTagNames = imageTagDaoJdbc.findByTagNames(List.of(tags),imageTagDaoOrderType,imageTagDaoOrder);
    log.debug("Success to search : " + byTagNames.size());
    return byTagNames;
  }
}
