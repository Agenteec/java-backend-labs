package ru.agenteec.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.agenteec.entity.RecipeEntity;
import ru.agenteec.repository.RecipeRepository;
import ru.agenteec.exception.RecipeNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    private RecipeRepository repository;

    @InjectMocks
    private RecipeServiceImpl service;

    private RecipeEntity testRecipe;

    @BeforeEach
    void setUp() {
        testRecipe = new RecipeEntity(1, "Borsch", 500);
    }

    @Test
    void findById_Success() {
        when(repository.findById(1)).thenReturn(testRecipe);

        RecipeEntity result = service.findById(1);

        assertNotNull(result);
        assertEquals("Borsch", result.getName());
        verify(repository, times(1)).findById(1);
    }

    @Test
    void findById_NotFound_ThrowsException() {
        when(repository.findById(99)).thenReturn(null);

        assertThrows(RecipeNotFoundException.class, () -> service.findById(99));
    }

    @Test
    void save_Success() {
        when(repository.save(any(RecipeEntity.class))).thenReturn(1);

        int id = service.save("Pasta", 600);

        assertEquals(1, id);
        verify(repository).save(any(RecipeEntity.class));
    }

    @Test
    void update_NotFound_ThrowsException() {
        when(repository.findById(1)).thenReturn(null);

        assertThrows(RecipeNotFoundException.class, () -> service.update(testRecipe));
        verify(repository, never()).update(any());
    }

    @Test
    void findAll_ReturnsList() {
        when(repository.findAll()).thenReturn(List.of(testRecipe));

        List<RecipeEntity> result = service.findAll();

        assertEquals(1, result.size());
        assertEquals("Borsch", result.get(0).getName());
    }
}