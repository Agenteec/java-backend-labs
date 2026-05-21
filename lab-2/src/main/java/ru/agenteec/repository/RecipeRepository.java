package ru.agenteec.repository;

import ru.agenteec.entity.RecipeEntity;
import ru.agenteec.exception.DatabaseException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeRepository {
    private final DataSource dataSource;

    public RecipeRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int save(RecipeEntity entity) {
        String sql = "INSERT INTO recipe (name, calories) VALUES (?, ?) RETURNING id";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getName());
            ps.setInt(2, entity.getCalories());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new DatabaseException("Failed to save recipe", null);
        } catch (SQLException e) {
            throw new DatabaseException("Error saving recipe", e);
        }
    }

    public RecipeEntity findById(int id) {
        String sql = "SELECT * FROM recipe WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new RecipeEntity(rs.getInt("id"), rs.getString("name"), rs.getInt("calories"));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding recipe by id", e);
        }
        return null;
    }

    public RecipeEntity findByField(String name) {
        String sql = "SELECT * FROM recipe WHERE name = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new RecipeEntity(rs.getInt("id"), rs.getString("name"), rs.getInt("calories"));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding recipe by name", e);
        }
        return null;
    }

    public List<RecipeEntity> findAll() {
        List<RecipeEntity> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipe";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                recipes.add(new RecipeEntity(rs.getInt("id"), rs.getString("name"), rs.getInt("calories")));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching all recipes", e);
        }
        return recipes;
    }

    public boolean update(RecipeEntity entity) {
        String sql = "UPDATE recipe SET name = ?, calories = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getName());
            ps.setInt(2, entity.getCalories());
            ps.setInt(3, entity.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating recipe", e);
        }
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM recipe WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting recipe", e);
        }
    }
}