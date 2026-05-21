package ru.agenteec.entity;

public class RecipeEntity {
    private Integer id;
    private String name;
    private Integer calories;

    public RecipeEntity() {}

    public RecipeEntity(String name, Integer calories) {
        this.name = name;
        this.calories = calories;
    }

    public RecipeEntity(Integer id, String name, Integer calories) {
        this.id = id;
        this.name = name;
        this.calories = calories;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getCalories() { return calories; }
    public void setCalories(Integer calories) { this.calories = calories; }

    @Override
    public String toString() {
        return "Recipe{id=" + id + ", name='" + name + "', calories=" + calories + "}";
    }
}