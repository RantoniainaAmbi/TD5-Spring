package com.td2.td5spring.repository;

import com.td2.td5spring.entity.Dish;
import com.td2.td5spring.entity.Ingredient;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

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

    public Optional<Dish> findById(int id) {
        String sql = "SELECT id, name, unit_price FROM dish WHERE id = ?";
        try {
            Dish dish = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                int dishId = rs.getInt("id");
                return new Dish(dishId, rs.getString("name"), rs.getDouble("unit_price"), findIngredientsByDishId(dishId));
            }, id);
            return Optional.ofNullable(dish);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private List<Ingredient> findIngredientsByDishId(Integer dishId) {
        String sql = "SELECT i.* FROM ingredient i JOIN dish_ingredient di ON i.id = di.id_ingredient WHERE di.id_dish = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Ingredient(
                rs.getInt("id"), rs.getString("name"), rs.getString("category"), rs.getDouble("price")
        ), dishId);
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