package io.github.usbharu.imagesearch.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import io.github.usbharu.imagesearch.domain.model.Group;
import io.github.usbharu.imagesearch.domain.repository.GroupDao;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;


class GroupServiceTest {

  @Mock
  Environment environment;

  @Mock
  GroupDao groupDao;

  @InjectMocks
  GroupService groupService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void validation_groupName_returnTrue() {
    when(environment.getProperty(anyString())).thenReturn("test");

    assertTrue(groupService.validation("test"));
  }

  @Test
  void validation_illegalGroupName_returnFalse() {
    when(environment.getProperty(anyString())).thenReturn(null);

    assertFalse(groupService.validation("illegalTest"));
  }

  @Test
  void validation_null_returnFalse() {
    assertFalse(groupService.validation(null));
  }

  @Test
  void validation_empty_throwIllegalArgumentException() {
    assertFalse(groupService.validation(""));
  }

  @Test
  void validation_blank_throwIllegalArgumentException() {
    assertFalse(groupService.validation(" "));
  }

  @Test
  void getGroup_getGroup_returnAllGroups() {
    when(environment.getProperty(anyString())).thenReturn("test");
    when(groupDao.findAll()).thenReturn(List.of(new Group("default"), new Group("test1")));
    assertEquals(2, groupService.getGroups().size());
  }

  @Test
  void getGroupAndAll_getGroupAndAll_returnAllGroupAndAll() {
    when(environment.getProperty(anyString())).thenReturn("test");

    when(groupDao.findAll()).thenReturn(List.of(new Group("default"), new Group("test1")));
    assertEquals(3, groupService.getGroupsAndAll().size());
  }
}
