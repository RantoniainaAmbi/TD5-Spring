package com.td2.td5spring.entity;

import lombok.Data;

@Data
public class CreateStockMovement {
    private UnitEnum unit;
    private Double value;
    private MovementTypeEnum type;
}