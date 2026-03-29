package com.td2.td5spring.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockMovement {
    private Integer id;
    private Integer ingredientId;
    private Double quantity;
    private String type;
    private Instant creationDatetime;
    private String unit;
}