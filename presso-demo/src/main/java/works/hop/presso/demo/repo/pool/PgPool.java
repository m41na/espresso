package works.hop.presso.demo.repo.pool;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class PgPool {

    private static PgPool instance;
    private final HikariDataSource ds;

    private PgPool(HikariDataSource ds) throws IOException {
        //hide constructor
        this.ds = ds;
    }

    public static PgPool instance() {
        if (instance == null) {
            synchronized (PgPool.class) {
                try {
                    Properties values = new Properties();
                    values.load(PgPool.class.getClassLoader().getResourceAsStream("database.properties"));
                    //configure datasource
                    HikariConfig config = new HikariConfig();
                    config.setJdbcUrl(values.getProperty("pg_url"));
                    config.setUsername(values.getProperty("pg_user"));
                    config.setPassword(values.getProperty("pg_password"));
                    config.addDataSourceProperty("cachePrepStmts", "true");
                    config.addDataSourceProperty("prepStmtCacheSize", "250");
                    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
                    //create datasource
                    instance = new PgPool(new HikariDataSource(config));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return instance;
    }

    public Connection connection() throws SQLException {
        return ds.getConnection();
    }
}
