package com.td2.td5spring.entity;

import lombok.Data;

@Data
public class CreateStockMovement {
    private String unit;
    private Double value;
    private String type;
}