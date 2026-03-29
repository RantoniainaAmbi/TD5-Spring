package com.td2.td5spring.repository;

import com.td2.td5spring.entity.Ingredient;
import com.td2.td5spring.entity.StockValue;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Repository
public class IngredientRepository {
    private final JdbcTemplate jdbcTemplate;

    public IngredientRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Ingredient> findAll() {
        String sql = "SELECT id, name, category, price FROM ingredient";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Ingredient(
                rs.getInt("id"), rs.getString("name"), rs.getString("category"), rs.getDouble("price")
        ));
    }

    public Ingredient findById(Integer id) {
        String sql = "SELECT id, name, category, price FROM ingredient WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Ingredient(
                rs.getInt("id"), rs.getString("name"), rs.getString("category"), rs.getDouble("price")
        ), id);
    }

    public StockValue getStockValue(Integer id, Instant at, String unit) {
        String sql = "SELECT unit, SUM(CASE WHEN type = 'OUT' THEN -quantity ELSE quantity END) as val " +
                "FROM stock_movement WHERE id_ingredient = ? AND creation_datetime <= ? AND unit = ? " +
                "GROUP BY unit";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new StockValue(
                rs.getString("unit"), rs.getDouble("val")
        ), id, Timestamp.from(at), unit);
    }
}