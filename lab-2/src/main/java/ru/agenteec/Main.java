package ru.agenteec;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.agenteec.entity.RecipeEntity;
import ru.agenteec.repository.RecipeRepository;

import java.util.Properties;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Properties props = new Properties();
        try (var is = Main.class.getClassLoader().getResourceAsStream("db.properties")) {
            props.load(is);
        } catch (Exception e) {
            log.error("Could not load db.properties");
            return;
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(props.getProperty("db.url"));
        config.setUsername(props.getProperty("db.user"));
        config.setPassword(props.getProperty("db.password"));

        try (HikariDataSource ds = new HikariDataSource(config)) {
            try (java.sql.Connection connection = ds.getConnection()) {
                Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
                Liquibase liquibase = new Liquibase("db/changelog/db.changelog-master.xml", new ClassLoaderResourceAccessor(), database);
                liquibase.update("");
                log.info("Migrations applied successfully");
            }

            RecipeRepository repository = new RecipeRepository(ds);

            int id = repository.save(new RecipeEntity("Borsch", 500));
            log.info("Saved recipe with ID: {}", id);

            RecipeEntity found = repository.findById(id);
            log.info("Found by ID: {}", found);

            RecipeEntity byName = repository.findByField("Borsch");
            log.info("Found by Name: {}", byName);

            found.setCalories(450);
            repository.update(found);
            log.info("Updated recipe: {}", repository.findById(id));

            log.info("All recipes: {}", repository.findAll());

            repository.deleteById(id);
            log.info("Deleted. Remaining: {}", repository.findAll().size());

        } catch (Exception e) {
            log.error("Application error", e);
        }
    }
}
