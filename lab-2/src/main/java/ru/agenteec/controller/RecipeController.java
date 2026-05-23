package ru.agenteec.controller;

import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import ru.agenteec.entity.RecipeEntity;
import ru.agenteec.exception.RecipeNotFoundException;
import ru.agenteec.service.RecipeService;

import static io.javalin.apibuilder.ApiBuilder.*;

public class RecipeController {
    private final RecipeService service;

    public RecipeController(RecipeService service) {
        this.service = service;
    }

    public void start(int port) {
        Javalin.create(config -> {

            config.routes.exception(RecipeNotFoundException.class, (e, ctx) -> {
                ctx.status(HttpStatus.NOT_FOUND);
                ctx.json(java.util.Map.of("error", e.getMessage()));
            });

            config.routes.apiBuilder(() -> {
                path("/recipes", () -> {
                    get(ctx -> ctx.json(service.findAll()));
                    post(ctx -> {
                        RecipeEntity recipe = ctx.bodyAsClass(RecipeEntity.class);
                        int id = service.save(recipe.getName(), recipe.getCalories());
                        ctx.status(HttpStatus.CREATED).result(String.valueOf(id));
                    });
                    path("/{id}", () -> {
                        get(ctx -> {
                            int id = Integer.parseInt(ctx.pathParam("id"));
                            ctx.json(service.findById(id));
                        });
                        delete(ctx -> {
                            int id = Integer.parseInt(ctx.pathParam("id"));
                            service.deleteById(id);
                            ctx.status(HttpStatus.NO_CONTENT);
                        });
                    });
                });
            });

        }).start(port);

        System.out.println("Server started on http://localhost:" + port);
    }
}