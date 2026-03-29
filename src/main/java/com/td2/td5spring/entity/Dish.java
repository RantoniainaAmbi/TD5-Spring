package com.td2.td5spring.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dish {
    private Integer id;
    private String name;
    private Double unitPrice;
    private List<Ingredient> ingredients;
}
