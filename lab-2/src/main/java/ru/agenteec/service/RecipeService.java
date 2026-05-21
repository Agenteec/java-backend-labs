package ru.agenteec.service;

import ru.agenteec.entity.RecipeEntity;
import java.util.List;

public interface RecipeService {
    int save(String name, int calories);
    RecipeEntity findById(int id);
    RecipeEntity findByField(String name);
    List<RecipeEntity> findAll();
    void update(RecipeEntity entity);
    void deleteById(int id);
}