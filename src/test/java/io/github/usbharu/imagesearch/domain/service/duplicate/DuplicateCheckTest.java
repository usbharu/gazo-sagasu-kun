package io.github.usbharu.imagesearch.domain.service.duplicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.brachtendorf.jimagehash.datastructures.tree.Result;
import dev.brachtendorf.jimagehash.hash.Hash;
import dev.brachtendorf.jimagehash.hashAlgorithms.HashingAlgorithm;
import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.repository.ImageDao;
import io.github.usbharu.imagesearch.util.ImageFileNameUtil;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.jdbc.core.JdbcTemplate;


class DuplicateCheckTest {

  @Mock
  ImageDao imageDao;

  @Mock
  JdbcTemplate jdbcTemplate;

  @Spy
  ImageFileNameUtil imageFileNameUtil;

  @Mock
  SQLDatabaseImageMatcherWrapper sqlDatabaseImageMatcherWrapper;

  @InjectMocks
  DuplicateCheck duplicateCheck;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }


  @Test
  void addAllImage_callAddImage_addAllImage() throws Exception {
    when(imageDao.findAll()).thenReturn(List.of(new Image(1, "0.jpg", "/testData/1/0.jpg"),
        new Image(2, "1.jpg", "/testData/1/1.jpg"), new Image(3, "2.jpg", "/testData/1/2.jpg")));
    doNothing().when(sqlDatabaseImageMatcherWrapper).addImage(anyString(), any(File.class));
    assertDoesNotThrow(() -> duplicateCheck.addAllImage());

    verify(sqlDatabaseImageMatcherWrapper, times(3)).addImage(anyString(), any(File.class));
  }


  @Test
  void addImage_addImage_addImage() throws SQLException, IOException {
    assertDoesNotThrow(() -> duplicateCheck.addImage(new Image(1, "0.jpg", "/testData/1/0.jpg")));

    verify(sqlDatabaseImageMatcherWrapper, times(1)).addImage(anyString(), any(File.class));
  }

  @Test
  void checkAll_checkAll_returnAllImages() {
    when(imageDao.findAll()).thenReturn(List.of(new Image(1, "0.jpg", "/testData/1/0.jpg"),
        new Image(2, "1.jpg", "/testData/1/1.jpg"), new Image(3, "2.jpg", "/testData/1/2.jpg")));
    when(sqlDatabaseImageMatcherWrapper.getAllMatchingImages()).thenReturn(
        Map.of("1", new PriorityQueue<>(List.of(new Result<>("2", 0.1, 0.1)))));
    when(imageDao.findById(eq(1))).thenReturn(new Image(1, "0.jpg", "/testData/1/0.jpg"));
    when(imageDao.findById(eq(2))).thenReturn(new Image(2, "1.jpg", "/testData/1/1.jpg"));
    when(imageDao.findById(eq(3))).thenReturn(new Image(3, "2.jpg", "/testData/1/2.jpg"));
    when(sqlDatabaseImageMatcherWrapper.findById(eq(1), any(HashingAlgorithm.class))).thenReturn(
        "test1".getBytes(StandardCharsets.UTF_8));
    when(sqlDatabaseImageMatcherWrapper.findById(eq(2), any(HashingAlgorithm.class))).thenReturn(
        "test2".getBytes(StandardCharsets.UTF_8));
    when(sqlDatabaseImageMatcherWrapper.findById(eq(3), any(HashingAlgorithm.class))).thenReturn(
        "test3".getBytes(StandardCharsets.UTF_8));
    when(sqlDatabaseImageMatcherWrapper.getSimilarImages(any(Hash.class), anyInt(),
        any(HashingAlgorithm.class))).thenReturn(
        List.of(new Result<>("1", 0.1, 0.1), new Result<>("2", 0.1, 0.1),
            new Result<>("3", 0.1, 0.1)));

    when(sqlDatabaseImageMatcherWrapper.reconstructHashFromDatabase(any(HashingAlgorithm.class),
        any(byte[].class))).thenReturn(
        new Hash(new BigInteger("test1".getBytes()), duplicateCheck.algo.getKeyResolution(),
            duplicateCheck.algo.algorithmId())).thenReturn(
        new Hash(new BigInteger("test2".getBytes()), duplicateCheck.algo.getKeyResolution(),
            duplicateCheck.algo.algorithmId())).thenReturn(
        new Hash(new BigInteger("test3".getBytes()), duplicateCheck.algo.getKeyResolution(),
            duplicateCheck.algo.algorithmId()));

    List<List<Image>> lists = duplicateCheck.checkAll();
    assertEquals(3, lists.size());
    for (List<Image> list : lists) {
      assertEquals(3, list.size());
    }

  }

  @Test
  void checkAll2_checkAll_returnAllImages() {
    List<Result<String>> results = List.of(new Result<>("2", 0.1, 0.1), new Result<>("3", 0.1, 0.1),
        new Result<>("1", 0.1, 0.1));
    when(sqlDatabaseImageMatcherWrapper.getAllMatchingImages()).thenReturn(
        Map.of("1", new PriorityQueue<>(results), "2", new PriorityQueue<>(results), "3",
            new PriorityQueue<>(results)));
    when(imageDao.findAll()).thenReturn(List.of(new Image(1, "0.jpg", "/testData/1/0.jpg"),
        new Image(2, "1.jpg", "/testData/1/1.jpg"), new Image(3, "2.jpg", "/testData/1/2.jpg")));

    when(imageDao.findById(eq(1))).thenReturn(new Image(1, "0.jpg", "/testData/1/0.jpg"));
    when(imageDao.findById(eq(2))).thenReturn(new Image(2, "1.jpg", "/testData/1/1.jpg"));
    when(imageDao.findById(eq(3))).thenReturn(new Image(3, "2.jpg", "/testData/1/2.jpg"));

    List<List<Image>> lists = duplicateCheck.checkAll2();
    assertEquals(3, lists.size());
    for (List<Image> list : lists) {
      assertEquals(3, list.size());
    }
  }
}
