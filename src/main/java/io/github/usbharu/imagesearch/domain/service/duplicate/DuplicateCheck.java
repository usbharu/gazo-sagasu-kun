package io.github.usbharu.imagesearch.domain.service.duplicate;

import dev.brachtendorf.jimagehash.datastructures.tree.Result;
import dev.brachtendorf.jimagehash.hash.Hash;
import dev.brachtendorf.jimagehash.hashAlgorithms.DifferenceHash;
import dev.brachtendorf.jimagehash.hashAlgorithms.DifferenceHash.Precision;
import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.repository.HashDao;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DuplicateCheck {

  final HashDao hashDao;
  final ImageDao imageDao;
  final ImageFileNameUtil imageFileNameUtil;
  final DifferenceHash algo;
  Logger logger = LoggerFactory.getLogger(DuplicateCheck.class);
  SQliteDatabaseImageMatcher databaseImageMatcher;

  public DuplicateCheck(ImageFileNameUtil imageFileNameUtil, ImageDao imageDao, HashDao hashDao,
      SQLDatabaseImageMatcherWrapper databaseImageMatcherWrapper) throws SQLException {
    Objects.requireNonNull(imageFileNameUtil, "ImageFileNameUtil is Null");
    Objects.requireNonNull(imageDao, "ImageDao is Null");
    databaseImageMatcher = databaseImageMatcherWrapper;
    this.imageFileNameUtil = imageFileNameUtil;
    algo = new DifferenceHash(32, Precision.Simple);
    databaseImageMatcher.addHashingAlgorithm(algo, 0.1);

    this.imageDao = imageDao;
    this.hashDao = hashDao;
  }

  public void addAllImage() {
    imageDao.findAll().forEach(this::addImage);

  }

  public void addImage(Image image) {
    Objects.requireNonNull(image, "Image is Null");
    try {
      databaseImageMatcher.addImage(String.valueOf(image.getId()),
          new File(imageFileNameUtil.getFullPath(image.getPath())));
    } catch (IOException | SQLException e) {
      logger.warn("Failed add image. Image: " + image, e);
    }
  }

  public List<List<Image>> checkAll() {
    return imageDao.findAll().stream().map(this::check).collect(Collectors.toList());
  }

  public List<List<Image>> checkAll2() {
    List<List<Image>> result = new ArrayList<>();
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
    return result;
  }

  public List<Image> check(Image image) {
    Objects.requireNonNull(image, "Image is Null");

    List<Image> result = new ArrayList<>();
    try {
      byte[] byId = databaseImageMatcher.findById(image.getId(), algo);
      if (byId == null) {
        return result;
      }
      Hash targetHash = databaseImageMatcher.reconstructHashFromDatabase(algo, byId);
      List<Result<String>> similarImages = databaseImageMatcher.getSimilarImages(targetHash,
          (int) (targetHash.getBitResolution() * 0.1), algo);
      for (Result<String> similarImage : similarImages) {
        Image resultImage = imageDao.findById(Integer.parseInt(similarImage.value));
        if (resultImage == null) {
          continue;
        }
        result.add(resultImage);
      }
    } catch (NumberFormatException e) {
      e.printStackTrace();
    }
    return result;

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
