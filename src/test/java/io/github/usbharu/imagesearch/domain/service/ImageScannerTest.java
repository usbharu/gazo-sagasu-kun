package io.github.usbharu.imagesearch.domain.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.usbharu.imagesearch.domain.model.Group;
import io.github.usbharu.imagesearch.domain.repository.GroupDao;
import io.github.usbharu.imagesearch.domain.repository.custom.BulkDao;
import io.github.usbharu.imagesearch.domain.service.scan.ScannerLoader;
import io.github.usbharu.imagesearch.domain.service.scan.impl.DefaultFilter;
import io.github.usbharu.imagesearch.domain.service.scan.impl.DefaultUnifier;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;

class ImageScannerTest {

  @Mock
  BulkDao bulkDao;

  @Mock
  GroupDao groupDao;

  @Mock
  ScannerLoader scannerLoader;

  @InjectMocks ImageScanner imageScanner;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void startScan_scanAll_addAll() throws NoSuchFieldException, IllegalAccessException, IOException {
//    when(imageScanner.getGroup()).thenReturn(Map.of("test","/testData/1/"));
//    when(imageScanner.getDepth()).thenReturn(3);
//    when(imageScanner.getFolder()).thenReturn("/testData/");
    Field group = imageScanner.getClass().getDeclaredField("group");
    group.trySetAccessible();
    group.set(imageScanner, Map.of("test","/testData/1/"));

    Field folder = imageScanner.getClass().getDeclaredField("folder");
    folder.trySetAccessible();
    folder.set(imageScanner, new ClassPathResource("/testData/").getFile().getAbsolutePath());

    doNothing().when(bulkDao).insertSplit(any(),anyInt());
    doNothing().when(bulkDao).delete();
    when(groupDao.insertOneWithReturnGroup(eq("default"))).thenReturn(new Group(286,"default"));
    when(scannerLoader.isSupported(any())).thenReturn(true);
    when(scannerLoader.getFilter()).thenReturn(new DefaultFilter());
    when(scannerLoader.getUnifier()).thenReturn(new DefaultUnifier());
    imageScanner.startScan();
    verify(bulkDao,times(1)).insertSplit(any(),anyInt());
  }
}
