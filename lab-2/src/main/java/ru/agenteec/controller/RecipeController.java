package ru.agenteec.controller;

import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import ru.agenteec.entity.RecipeEntity;
import ru.agenteec.exception.RecipeNotFoundException;
import ru.agenteec.service.RecipeService;

public class RecipeController {
    private final RecipeService service;

    public RecipeController(RecipeService service) {
        this.service = service;
    }

    public void start(int port) {
        Javalin app = Javalin.create().start(port);

        app.exception(RecipeNotFoundException.class, (e, ctx) -> {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(java.util.Map.of("error", e.getMessage()));
        });

        app.get("/recipes", ctx -> ctx.json(service.findAll()));

        app.get("/recipes/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.json(service.findById(id));
        });

        app.post("/recipes", ctx -> {
            RecipeEntity recipe = ctx.bodyAsClass(RecipeEntity.class);
            int id = service.save(recipe.getName(), recipe.getCalories());
            ctx.status(HttpStatus.CREATED).result(String.valueOf(id));
        });

        app.delete("/recipes/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            service.deleteById(id);
            ctx.status(HttpStatus.NO_CONTENT);
        });

        System.out.println("Server started on http://localhost:" + port);
    }
}