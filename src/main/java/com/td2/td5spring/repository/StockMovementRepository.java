package com.td2.td5spring.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class StockMovementRepository {
    private final JdbcTemplate jdbcTemplate;

    public StockMovementRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}