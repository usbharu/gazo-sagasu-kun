package io.github.usbharu.imagesearch.domain.service.duplicate;

import dev.brachtendorf.jimagehash.matcher.persistent.database.DatabaseImageMatcher;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SQLDatabaseImageMatcherWrapper extends SQliteDatabaseImageMatcher{
  private DatabaseImageMatcher databaseImageMatcher;

  final
  JdbcTemplate jdbcTemplate;

  public SQLDatabaseImageMatcherWrapper(JdbcTemplate jdbcTemplate) throws SQLException {
    super(null,jdbcTemplate);

    this.jdbcTemplate = jdbcTemplate;
  }

  public DatabaseImageMatcher getDatabaseImageMatcher() throws SQLException {
    this.databaseImageMatcher = new SQliteDatabaseImageMatcher(null,jdbcTemplate);
    return databaseImageMatcher;
  }
}
