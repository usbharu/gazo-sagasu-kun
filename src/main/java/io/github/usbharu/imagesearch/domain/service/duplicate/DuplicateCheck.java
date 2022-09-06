package io.github.usbharu.imagesearch.domain.service.duplicate;

import dev.brachtendorf.jimagehash.datastructures.tree.Result;
import dev.brachtendorf.jimagehash.hashAlgorithms.DifferenceHash;
import dev.brachtendorf.jimagehash.hashAlgorithms.DifferenceHash.Precision;
import dev.brachtendorf.jimagehash.matcher.persistent.database.DatabaseImageMatcher;
import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.repository.ImageDao;
import io.github.usbharu.imagesearch.util.ImageFileNameUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DuplicateCheck {

  final
  ImageDao imageDao;

  final
  ImageFileNameUtil imageFileNameUtil;
  final JdbcTemplate jdbcTemplate;
  DatabaseImageMatcher databaseImageMatcher;

  public DuplicateCheck(JdbcTemplate jdbcTemplate,
      ImageFileNameUtil imageFileNameUtil,
      ImageDao imageDao)
      throws SQLException {
    Objects.requireNonNull(jdbcTemplate, "JdbcTemplate is Null");
    Objects.requireNonNull(imageFileNameUtil, "ImageFileNameUtil is Null");
    Objects.requireNonNull(imageDao, "ImageDao is Null");
    this.jdbcTemplate = jdbcTemplate;
    synchronized (this.jdbcTemplate) {
      databaseImageMatcher =
          new SQliteDatabaseImageMatcher(null,
              this.jdbcTemplate);
      this.imageFileNameUtil = imageFileNameUtil;
      databaseImageMatcher.addHashingAlgorithm(new DifferenceHash(32, Precision.Simple), 0.1);
    }
    this.imageDao = imageDao;
  }

  public void addAllImage() {
    synchronized (jdbcTemplate) {
      imageDao.findAll().forEach(this::addImage);
    }
  }

  public void addImage(Image image) {
    Objects.requireNonNull(image, "Image is Null");
    try {
      databaseImageMatcher.addImage(String.valueOf(image.getId()),
          new File(imageFileNameUtil.getFullPath(image.getPath())));
    } catch (IOException | SQLException e) {
      e.printStackTrace();
    }
  }

  public List<List<Image>> checkAll() {
    return imageDao.findAll().stream().map(this::check).collect(Collectors.toList());
  }

  public List<List<Image>> checkAll2() {
    List<List<Image>> result = new ArrayList<>();
    try {
      Map<String, PriorityQueue<Result<String>>> allMatchingImages =
          databaseImageMatcher.getAllMatchingImages();
      List<Image> images = new ArrayList<>();
      for (Entry<String, PriorityQueue<Result<String>>> stringPriorityQueueEntry : allMatchingImages.entrySet()) {
        images.clear();
        for (Result<String> stringResult : stringPriorityQueueEntry.getValue()) {
          images.add(imageDao.findById(Integer.parseInt(stringResult.value)));
        }
        result.add(images);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }

  public List<Image> check(Image image) {
    Objects.requireNonNull(image, "Image is Null");
    return check(new File(imageFileNameUtil.getFullPath(image.getPath())));
  }

  public List<Image> check(File image) {
    Objects.requireNonNull(image, "Image is Null");
    if (!image.exists()) {
      throw new UncheckedIOException("Image is not found", new FileNotFoundException());

    }
    if (!image.isFile()) {
      throw new IllegalArgumentException("Image is not File");
    }
    List<Image> result = new ArrayList<>();
    try {
      PriorityQueue<Result<String>> matchingImages = databaseImageMatcher.getMatchingImages(image);
      for (Result<String> matchingImage : matchingImages) {
        result.add(imageDao.findById(Integer.parseInt(matchingImage.value)));
      }
    } catch (SQLException ignored) {
      // SQLite実装では発生しない
    } catch (IOException e) {
      throw new UncheckedIOException("Image has problem", e);
    }
    return result;
  }
}
