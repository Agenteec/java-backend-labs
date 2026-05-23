package ru.agenteec.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.agenteec.entity.RecipeEntity;
import ru.agenteec.exception.RecipeNotFoundException;
import ru.agenteec.repository.RecipeRepository;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    private RecipeRepository repository;

    @InjectMocks
    private RecipeServiceImpl service;

    @Test
    @DisplayName("Успешное сохранение рецепта")
    void save_ShouldReturnId() {
        String name = "Pasta";
        int calories = 400;
        when(repository.save(any(RecipeEntity.class))).thenReturn(10);

        int result = service.save(name, calories);

        assertEquals(10, result);
        verify(repository).save(argThat(r -> r.getName().equals(name) && r.getCalories() == calories));
    }

    @Test
    @DisplayName("Поиск по ID: Рецепт найден")
    void findById_ShouldReturnRecipe_WhenExists() {
        RecipeEntity expected = new RecipeEntity(1, "Soup", 200);
        when(repository.findById(1)).thenReturn(expected);

        RecipeEntity actual = service.findById(1);

        assertNotNull(actual);
        assertEquals("Soup", actual.getName());
    }

    @Test
    @DisplayName("Поиск по ID: Бросает исключение, если не найден")
    void findById_ShouldThrowException_WhenNotFound() {
        when(repository.findById(1)).thenReturn(null);

        assertThrows(RecipeNotFoundException.class, () -> service.findById(1));
    }

    @Test
    @DisplayName("Поиск по имени: Рецепт найден")
    void findByField_ShouldReturnRecipe_WhenExists() {
        RecipeEntity expected = new RecipeEntity(1, "Salad", 150);
        when(repository.findByField("Salad")).thenReturn(expected);

        RecipeEntity actual = service.findByField("Salad");

        assertEquals(150, actual.getCalories());
    }

    @Test
    @DisplayName("Поиск по имени: Бросает исключение, если не найден")
    void findByField_ShouldThrowException_WhenNotFound() {
        when(repository.findByField("Unknown")).thenReturn(null);

        assertThrows(RecipeNotFoundException.class, () -> service.findByField("Unknown"));
    }

    @Test
    @DisplayName("Успешное обновление существующего рецепта")
    void update_ShouldCallRepository_WhenRecipeExists() {
        RecipeEntity recipe = new RecipeEntity(1, "Old Name", 100);
        when(repository.findById(1)).thenReturn(recipe);
        when(repository.update(recipe)).thenReturn(true);

        service.update(recipe);

        verify(repository).update(recipe);
    }

    @Test
    @DisplayName("Обновление: Бросает исключение, если рецепт для обновления не найден")
    void update_ShouldThrowException_WhenNotFound() {
        RecipeEntity recipe = new RecipeEntity(1, "New Name", 100);
        when(repository.findById(1)).thenReturn(null);

        assertThrows(RecipeNotFoundException.class, () -> service.update(recipe));
        verify(repository, never()).update(any());
    }

    @Test
    @DisplayName("Получение всех рецептов")
    void findAll_ShouldReturnList() {
        List<RecipeEntity> list = List.of(new RecipeEntity("R1", 100), new RecipeEntity("R2", 200));
        when(repository.findAll()).thenReturn(list);

        List<RecipeEntity> result = service.findAll();

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Удаление рецепта")
    void deleteById_ShouldCallRepository() {
        service.deleteById(1);
        verify(repository).deleteById(1);
    }
}