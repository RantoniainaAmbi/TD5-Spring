package com.td2.td5spring.controller;

import com.td2.td5spring.entity.Dish;
import com.td2.td5spring.entity.Ingredient;
import com.td2.td5spring.repository.DishRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/dishes")
public class DishController {
    private final DishRepository dishRepository;

    public DishController(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @GetMapping
    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    @PutMapping("/{id}/ingredients")
    public void updateIngredients(
            @PathVariable int id,
            @RequestBody(required = false) List<Ingredient> ingredients) {

        if (ingredients == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Request body containing ingredients is mandatory.");
        }



        dishRepository.updateAssociations(id, ingredients);
    }
}