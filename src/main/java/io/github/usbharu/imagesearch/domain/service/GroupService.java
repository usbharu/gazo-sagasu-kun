package io.github.usbharu.imagesearch.domain.service;

import static io.github.usbharu.imagesearch.domain.validation.Validation.require;

import io.github.usbharu.imagesearch.domain.model.Group;
import io.github.usbharu.imagesearch.domain.repository.GroupDao;
import io.github.usbharu.imagesearch.domain.validation.StringValidation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

  private final Environment environment;
  private final GroupDao groupDao;

  public GroupService(GroupDao groupDao, Environment environment) {
    Objects.requireNonNull(groupDao,"GroupDao is Null");
    Objects.requireNonNull(environment,"Environment(Args) is Null");
    this.groupDao = groupDao;
    this.environment = environment;
  }

  public List<Group> getGroups() {
    return groupDao.findAll();
  }

  public List<Group> getGroupsAndAll() {

    ArrayList<Group> all = new ArrayList<>(groupDao.findAll());
    all.add(new Group("all"));
    return all;
  }

  public boolean validation(String group) {
    require().nonNullAndNonBlank(group);
    return environment.getProperty("imagesearch.scan.group." + group) != null;
  }
}
