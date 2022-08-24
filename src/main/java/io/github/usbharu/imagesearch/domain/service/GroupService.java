package io.github.usbharu.imagesearch.domain.service;

import io.github.usbharu.imagesearch.domain.model.Group;
import io.github.usbharu.imagesearch.domain.repository.GroupDao;
import java.util.List;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

  private final Environment environment;
  private final GroupDao groupDao;

  public GroupService(GroupDao groupDao, Environment environment) {
    this.groupDao = groupDao;
    this.environment = environment;
  }

  public List<Group> getGroups() {
    return groupDao.findAll();
  }

  public List<Group> getGroupsAndAll() {
    List<Group> all = groupDao.findAll();
    all.add(new Group("all"));
    return all;
  }

  public boolean validation(String group) {
    return environment.getProperty("imagesearch.scan.group." + group) != null;
  }
}
