package io.github.usbharu.imagesearch.domain.service;

import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.repository.DynamicSearchBuilder;
import io.github.usbharu.imagesearch.domain.repository.DynamicSearchDao;
import io.github.usbharu.imagesearch.domain.repository.ImageDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

  private final DynamicSearchDao dynamicSearchDao;



  @Autowired
  public ImageService(DynamicSearchDao dynamicSearchDao) {
    this.dynamicSearchDao = dynamicSearchDao;

  }

  public Image findById(int id) {
    List<Image> search =
        dynamicSearchDao.search(new DynamicSearchBuilder().setId(id).createDynamicSearch());
    if (search.size()==0) {
      return new Image("not found","");
    }
    return search.get(0);
  }
}
