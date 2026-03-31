package com.td2.td5spring.repository;

import com.td2.td5spring.entity.StockMovement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Repository
public class StockMovementRepository {
    private final JdbcTemplate jdbcTemplate;

    public StockMovementRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<StockMovement> findByIngredientIdAndDateRange(int ingredientId, Instant from, Instant to) {
        String sql = "SELECT id, creation_datetime, unit, quantity, type " +
                "FROM stock_movement " +
                "WHERE id_ingredient = ? AND creation_datetime >= ? AND creation_datetime <= ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new StockMovement(
                rs.getInt("id"),
                rs.getInt("id_ingredient"),
                rs.getDouble("quantity"),
                rs.getString("type"),
                rs.getTimestamp("creation_datetime").toInstant(),
                rs.getString("unit")
        ), ingredientId, Timestamp.from(from), Timestamp.from(to));
    }
}