package ru.agenteec.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

public class DatabaseConfig {
    private static final Logger log = LoggerFactory.getLogger(DatabaseConfig.class);

    public static HikariDataSource createDataSource() {
        Properties props = new Properties();
        try (InputStream is = DatabaseConfig.class.getClassLoader().getResourceAsStream("db/db.properties")) {
            if (is == null) throw new RuntimeException("db.properties not found");
            props.load(is);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load db config", e);
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(props.getProperty("db.url"));
        config.setUsername(props.getProperty("db.user"));
        config.setPassword(props.getProperty("db.password"));
        config.setDriverClassName("org.postgresql.Driver");
        return new HikariDataSource(config);
    }

    public static void runMigrations(DataSource dataSource) {
        log.info("Running Liquibase migrations...");
        try (Connection connection = dataSource.getConnection()) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db/changelog/db.changelog-master.xml",
                    new ClassLoaderResourceAccessor(), database);
            liquibase.update("");
        } catch (Exception e) {
            throw new RuntimeException("Migration failed", e);
        }
    }
}