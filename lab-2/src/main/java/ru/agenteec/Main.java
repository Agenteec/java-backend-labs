package ru.agenteec;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.agenteec.config.DatabaseConfig;
import ru.agenteec.controller.RecipeController;
import ru.agenteec.repository.RecipeRepository;
import ru.agenteec.service.RecipeServiceImpl;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        HikariDataSource ds = DatabaseConfig.createDataSource();

        try {
            DatabaseConfig.runMigrations(ds);

            RecipeRepository repository = new RecipeRepository(ds);
            RecipeServiceImpl service = new RecipeServiceImpl(repository);

            log.info("App initialized. Total recipes: {}", service.findAll().size());

            new RecipeController(service).start(7070);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log.info("Shutting down database pool...");
                ds.close();
            }));

        } catch (Exception e) {
            log.error("Fatal error during startup", e);
            ds.close();
            System.exit(1);
        }
    }
}