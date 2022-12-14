package io.github.usbharu.imagesearch.domain.service.duplicate;

import dev.brachtendorf.jimagehash.datastructures.tree.Result;
import dev.brachtendorf.jimagehash.hash.Hash;
import dev.brachtendorf.jimagehash.hashAlgorithms.HashingAlgorithm;
import dev.brachtendorf.jimagehash.matcher.persistent.database.DatabaseImageMatcher;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class SQliteDatabaseImageMatcher extends DatabaseImageMatcher {

  private final JdbcTemplate jdbcTemplate;

  Logger logger = LoggerFactory.getLogger(SQliteDatabaseImageMatcher.class);

  /**
   * Attempts to establish a connection to the given database using the supplied connection object.
   * If the database does not yet exist an empty db will be initialized.
   *
   * @param connection the database connection
   * @throws SQLException        if a database access error occurs {@code null}
   * @throws SQLTimeoutException when the driver has determined that the timeout value specified by
   *                             the {@code setLoginTimeout} method has been exceeded and has at
   *                             least tried to cancel the current database connection attempt
   */
  public SQliteDatabaseImageMatcher(Connection connection, JdbcTemplate jdbcTemplate)
      throws SQLException {
    super(connection);
    this.jdbcTemplate = jdbcTemplate;
    jdbcTemplate.update(
        "CREATE TABLE IF NOT EXISTS ImageHasher (Id INTEGER PRIMARY KEY ,SerializeData BLOB)");
  }

  @Override
  protected void initialize(Connection conn) {
    this.conn = conn;
  }

  @Override
  public void serializeToDatabase(int id) throws SQLException {
    PreparedStatement ps =
        conn.prepareStatement("INSERT OR REPLACE INTO ImageHasher (Id,SerializeData) VALUES(?,?)");
    PipedOutputStream pipeOut = new PipedOutputStream();
    try {
      PipedInputStream pipe = new PipedInputStream(pipeOut);
      ObjectOutputStream oos = new ObjectOutputStream(pipeOut);
      oos.writeObject(this);
      oos.close();
      ps.setInt(1, id);
      ps.setBinaryStream(2, pipe);
      ps.execute();
    } catch (IOException e) {
      // should not occur
      e.printStackTrace();
    }

  }

  @Override
  protected boolean doesTableExist(String tableName) {
    try {
      synchronized (jdbcTemplate) {
        jdbcTemplate.queryForMap(
            "SELECT name FROM main.sqlite_master WHERE tbl_name = ? AND type = 'table'", tableName);
      }
    } catch (EmptyResultDataAccessException e) {
      return false;
    }
    return true;
  }

  @Override
  protected void addImage(HashingAlgorithm hashAlgo, String url, BufferedImage image)
      throws SQLException {

    String tableName = resolveTableName(hashAlgo);

    if (!doesTableExist(tableName)) {
      createHashTable(hashAlgo);
    }

    Hash hash = hashAlgo.hash(image);
    synchronized (jdbcTemplate) {
      jdbcTemplate.update("INSERT OR REPLACE INTO " + tableName + " (url,hash) VALUES (?,?)", url,
          hash.toByteArray());
    }
  }

  @Override
  public boolean doesEntryExist(String uniqueId, HashingAlgorithm hashAlgo) {
    try {
      jdbcTemplate.queryForMap("SELECT * FROM " + resolveTableName(hashAlgo) + " WHERE URL = ?",
          uniqueId);
    } catch (EmptyResultDataAccessException e) {
      return false;
    }

    return false;
  }

  @Override
  public List<Result<String>> getSimilarImages(Hash targetHash, int maxDistance,
      HashingAlgorithm hasher) {
    String tableName = resolveTableName(hasher);
    List<Result<String>> uniqueIds = new ArrayList<>();
    synchronized (jdbcTemplate) {
      List<Map<String, Object>> maps =
          jdbcTemplate.queryForList("SELECT url,hash FROM " + tableName);
      for (Map<String, Object> map : maps) {
        byte[] bytes = (byte[]) map.get("hash");
        Hash h = reconstructHashFromDatabase(hasher, bytes);
        int distance = targetHash.hammingDistanceFast(h);
        double normalizedDistance = distance / (double) targetHash.getBitResolution();
        if (distance <= maxDistance) {
          String url = (String) map.get("url");
          uniqueIds.add(new Result<String>(url, distance, normalizedDistance));
        }
      }
    }
    return uniqueIds;
  }

  @Override
  public Map<String, PriorityQueue<Result<String>>> getAllMatchingImages() {
    Map<String, PriorityQueue<Result<String>>> result = new HashMap<>();

    HashingAlgorithm algorithm = steps.keySet().iterator().next();

    String tableName = resolveTableName(algorithm);

    List<Map<String, Object>> maps = jdbcTemplate.queryForList("SELECT url FROM " + tableName);
    for (Map<String, Object> map : maps) {
      String uniqueId = (String) map.get("url");

      PriorityQueue<Result<String>> results = null;

      for (Entry<HashingAlgorithm, AlgoSettings> hashingAlgorithmAlgoSettingsEntry : steps.entrySet()) {
        HashingAlgorithm algo = hashingAlgorithmAlgoSettingsEntry.getKey();

        Map<String, Object> stringObjectMap = jdbcTemplate.queryForMap(
            "SELECT hash FROM " + resolveTableName(algo) + " WHERE url = ?", uniqueId);
        byte[] bytes = (byte[]) stringObjectMap.get("hash");
        Hash targetHash = reconstructHashFromDatabase(algo, bytes);
        AlgoSettings settings = hashingAlgorithmAlgoSettingsEntry.getValue();

        int threshold = 0;
        if (settings.isNormalized()) {
          int hashLength = targetHash.getBitResolution();
          threshold = (int) Math.round(settings.getThreshold() * hashLength);
        } else {
          threshold = (int) settings.getThreshold();
        }
        PriorityQueue<Result<String>> temp =
            new PriorityQueue<>(getSimilarImages(targetHash, threshold, algo));

        if (results != null) {
          temp.retainAll(results);
        }
        results = temp;


      }
      result.put(uniqueId, results);
    }
    return result;
  }

  @Override
  protected void createHashTable(HashingAlgorithm hasher) {
    String tableName = resolveTableName(hasher);

    if (doesTableExist(tableName)) {
      return;
    }
    jdbcTemplate.update(
        "CREATE TABLE IF NOT EXISTS " + tableName + " (url TEXT PRIMARY KEY , hash BLOB )");
  }

  @Override
  public Hash reconstructHashFromDatabase(HashingAlgorithm hasher, byte[] bytes) {
    return super.reconstructHashFromDatabase(hasher, bytes);
  }

  public byte[] findById(int id, HashingAlgorithm algorithm) {
    try {

      Map<String, Object> stringObjectMap =
          jdbcTemplate.queryForMap(
              "SELECT hash FROM " + resolveTableName(algorithm) + " WHERE url = ?", id);
      return (byte[]) stringObjectMap.get("hash");
    } catch (EmptyResultDataAccessException e) {
      logger.warn(id + " was not found", e);
      return null;
    }
  }

  @Override
  public void close() throws SQLException {
  }
}
