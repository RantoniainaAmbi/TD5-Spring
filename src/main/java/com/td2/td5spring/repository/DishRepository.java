package com.td2.td5spring.repository;

import com.td2.td5spring.entity.Dish;
import com.td2.td5spring.entity.Ingredient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class DishRepository {
    private final JdbcTemplate jdbcTemplate;

    public DishRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Dish> findAll() {
        String sql = "SELECT id, name, unit_price FROM dish";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Integer dishId = rs.getInt("id");
            List<Ingredient> ingredients = findIngredientsByDishId(dishId);
            return new Dish(dishId, rs.getString("name"), rs.getDouble("unit_price"), ingredients);
        });
    }

    private List<Ingredient> findIngredientsByDishId(Integer dishId) {
        String sql = "SELECT i.* FROM ingredient i JOIN dish_ingredient di ON i.id = di.id_ingredient WHERE di.id_dish = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Ingredient(
                rs.getInt("id"), rs.getString("name"), rs.getString("category"), rs.getDouble("price")
        ), dishId);
    }

    public void updateDishIngredients(Integer dishId, List<Ingredient> ingredients) {
        jdbcTemplate.update("DELETE FROM dish_ingredient WHERE id_dish = ?", dishId);
        for (Ingredient ing : ingredients) {
            jdbcTemplate.update("INSERT INTO dish_ingredient (id_dish, id_ingredient) VALUES (?, ?)", dishId, ing.getId());
        }
    }

    public void updateAssociations(int dishId, List<Ingredient> ingredients) {
        String deleteSql = "DELETE FROM dish_ingredient WHERE id_dish = ?";
        jdbcTemplate.update(deleteSql, dishId);


        String insertSql = "INSERT INTO dish_ingredient (id_dish, id_ingredient) " +
                "SELECT ?, id FROM ingredient WHERE id = ?";

        for (Ingredient ing : ingredients) {
            jdbcTemplate.update(insertSql, dishId, ing.getId());
        }
    }
}