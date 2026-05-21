package ru.agenteec.service;

import ru.agenteec.entity.RecipeEntity;
import ru.agenteec.repository.RecipeRepository;
import ru.agenteec.exception.RecipeNotFoundException;

import java.util.List;

public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository repository;

    public RecipeServiceImpl(RecipeRepository repository) {
        this.repository = repository;
    }

    @Override
    public int save(String name, int calories) {
        return repository.save(new RecipeEntity(name, calories));
    }

    @Override
    public RecipeEntity findById(int id) {
        RecipeEntity recipe = repository.findById(id);
        if (recipe == null) {
            throw new RecipeNotFoundException("Recipe with id " + id + " not found");
        }
        return recipe;
    }

    @Override
    public RecipeEntity findByField(String name) {
        RecipeEntity recipe = repository.findByField(name);
        if (recipe == null) {
            throw new RecipeNotFoundException("Recipe with name '" + name + "' not found");
        }
        return recipe;
    }

    @Override
    public List<RecipeEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public void update(RecipeEntity entity) {
        findById(entity.getId());
        repository.update(entity);
    }

    @Override
    public void deleteById(int id) {
        repository.deleteById(id);
    }
}