package io.github.usbharu.imagesearch.domain.service;

import io.github.usbharu.imagesearch.domain.model.TagCount;
import io.github.usbharu.imagesearch.domain.repository.TagDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {

  @Autowired
  private TagDao tagDao;

  public List<TagCount> tagOrderOfMostUsed() {
    return tagDao.tagCount();
  }

  public List<TagCount> tagOrderOfMostUsedLimit(int limit) {
    return tagDao.tagCount(limit);
  }
}
