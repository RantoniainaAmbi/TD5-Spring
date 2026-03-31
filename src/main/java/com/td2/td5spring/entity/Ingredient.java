package com.td2.td5spring.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ingredient {
    private Integer id;
    private String name;
    private CategoryEnum category;
    private Double price;
}