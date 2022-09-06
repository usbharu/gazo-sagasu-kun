package io.github.usbharu.imagesearch.image.duplicate;

import dev.brachtendorf.jimagehash.datastructures.tree.Result;
import dev.brachtendorf.jimagehash.hashAlgorithms.DifferenceHash;
import dev.brachtendorf.jimagehash.hashAlgorithms.DifferenceHash.Precision;
import dev.brachtendorf.jimagehash.matcher.persistent.database.DatabaseImageMatcher;
import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.repository.ImageDao;
import io.github.usbharu.imagesearch.util.ImageFileNameUtil;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DuplicateCheck {

  final
  ImageDao imageDao;

  final
  ImageFileNameUtil imageFileNameUtil;
  JdbcTemplate jdbcTemplate;
  DatabaseImageMatcher databaseImageMatcher;

  public DuplicateCheck(JdbcTemplate jdbcTemplate, ImageFileNameUtil imageFileNameUtil,
      ImageDao imageDao)
      throws SQLException {
    this.jdbcTemplate = jdbcTemplate;
    databaseImageMatcher = new SQLLiteDatabaseMatcher(jdbcTemplate.getDataSource().getConnection());
    this.imageFileNameUtil = imageFileNameUtil;
    databaseImageMatcher.addHashingAlgorithm(new DifferenceHash(32, Precision.Simple), 0.4);
    this.imageDao = imageDao;
  }

  public void addImage(Image image) {
    try {
      databaseImageMatcher.addImage(String.valueOf(image.getId()),
          new File(imageFileNameUtil.getFullPath(image.getPath())));
    } catch (IOException | SQLException e) {
      e.printStackTrace();
    }
  }

  public List<Image> check(File image) {
    List<Image> result = new ArrayList<>();
    try {
      PriorityQueue<Result<String>> matchingImages = databaseImageMatcher.getMatchingImages(image);
      for (Result<String> matchingImage : matchingImages) {
        result.add(imageDao.findById(Integer.parseInt(matchingImage.value)));
      }
    } catch (SQLException | IOException e) {
      e.printStackTrace();
    }
    return result;
  }
}
