package io.github.usbharu.imagesearch.image.duplicate;

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

public class SQLLiteDatabaseMatcher extends DatabaseImageMatcher {

  /**
   * Attempts to establish a connection to the given database using the supplied connection object. If
   * the database does not yet exist an empty db will be initialized.
   *
   * @param connection the database connection
   * @throws SQLException        if a database access error occurs {@code null}
   * @throws SQLTimeoutException when the driver has determined that the timeout value specified by
   *                             the {@code setLoginTimeout} method has been exceeded and has at least
   *                             tried to cancel the current database connection attempt
   */
  public SQLLiteDatabaseMatcher(Connection connection) throws SQLException {
    super(connection);
  }

  @Override
  public void serializeToDatabase(int id) throws SQLException {
    PreparedStatement ps = conn.prepareStatement("INSERT OR REPLACE INTO ImageHasher (Id,SerializeData) VALUES(?,?)");
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
  protected void addImage(HashingAlgorithm hashAlgo, String url, BufferedImage image)
      throws SQLException {
    String tableName = resolveTableName(hashAlgo);

    if (!doesTableExist(tableName)) {
      createHashTable(hashAlgo);
    }
    try (PreparedStatement insertHash = conn
        .prepareStatement("INSERT OR REPLACE INTO " + tableName + " (url,hash) VALUES(?,?)")) {
      Hash hash = hashAlgo.hash(image);
      insertHash.setString(1, url);
      insertHash.setBytes(2, hash.toByteArray());
      insertHash.execute();
    }
  }
}
