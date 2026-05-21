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
import ru.agenteec.repository.RecipeRepository;
import ru.agenteec.service.RecipeService;
import ru.agenteec.service.RecipeServiceImpl;
import ru.agenteec.controller.RecipeController;

import java.io.InputStream;
import java.sql.Connection;

import java.util.Properties;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("Starting Recipe Application...");

        Properties props = new Properties();
        try (InputStream is = Main.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is == null) {
                log.error("Could not find db.properties in resources!");
                return;
            }
            props.load(is);
        } catch (Exception e) {
            log.error("Error loading database properties", e);
            return;
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(props.getProperty("db.url"));
        config.setUsername(props.getProperty("db.user"));
        config.setPassword(props.getProperty("db.password"));
        config.setDriverClassName("org.postgresql.Driver");

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(20000);

        try (HikariDataSource dataSource = new HikariDataSource(config)) {

            runMigrations(dataSource);

            RecipeRepository repository = new RecipeRepository(dataSource);

            RecipeService service = new RecipeServiceImpl(repository);

            RecipeController controller = new RecipeController(service);

            log.info("Application layers initialized successfully.");

            demoService(service);

            controller.start(8080);

        } catch (Exception e) {
            log.error("Critical application error", e);
        }
    }


    private static void runMigrations(HikariDataSource ds) {
        log.info("Running Liquibase migrations...");
        try (Connection connection = ds.getConnection()) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase(
                    "db/changelog/db.changelog-master.xml",
                    new ClassLoaderResourceAccessor(),
                    database
            );

            liquibase.update("");
            log.info("Database migrations applied successfully.");
        } catch (Exception e) {
            log.error("Migration failed!", e);
            throw new RuntimeException("Could not update database schema", e);
        }
    }


    private static void demoService(RecipeService service) {
        log.info("--- Service Layer Demo ---");
        try {
            int id = service.save("Pasta Carbonara", 650);
            log.info("Created recipe with ID: {}", id);

            var recipe = service.findById(id);
            log.info("Read recipe: {}", recipe);

            recipe.setName("Updated Carbonara");
            service.update(recipe);
            log.info("Updated name to: {}", service.findById(id).getName());

            log.info("Current total recipes in DB: {}", service.findAll().size());

        } catch (Exception e) {
            log.warn("Demo notice: {}", e.getMessage());
        }
        log.info("--- Demo finished ---");
    }
}
