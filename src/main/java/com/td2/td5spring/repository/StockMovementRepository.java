package com.td2.td5spring.repository;

import com.td2.td5spring.entity.CreateStockMovement;
import com.td2.td5spring.entity.MovementTypeEnum;
import com.td2.td5spring.entity.StockMovement;
import com.td2.td5spring.entity.UnitEnum;
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
        String sql = "SELECT id, id_ingredient, quantity, unit, type, creation_datetime " +
                "FROM stock_movement " +
                "WHERE id_ingredient = ? " +
                "AND creation_datetime >= ? " +
                "AND creation_datetime <= ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new StockMovement(
                        rs.getInt("id"),
                        rs.getInt("id_ingredient"),
                        rs.getDouble("quantity"),
                        MovementTypeEnum.valueOf(rs.getString("type").toUpperCase()),
                        rs.getTimestamp("creation_datetime").toInstant(),
                        UnitEnum.valueOf(rs.getString("unit").toUpperCase())
                ),
                ingredientId,
                Timestamp.from(from),
                Timestamp.from(to)
        );
    }

    public StockMovement save(int ingredientId, CreateStockMovement create) {
        String sql = "INSERT INTO stock_movement (id_ingredient, unit, quantity, type, creation_datetime) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING id, creation_datetime";

        Instant now = Instant.now();

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new StockMovement(
                rs.getInt("id"),
                ingredientId,
                create.getValue(),
                create.getType(),
                rs.getTimestamp("creation_datetime").toInstant(),
                create.getUnit()
        ), ingredientId, create.getUnit().name(), create.getValue(), create.getType().name(), Timestamp.from(now));
    }
}