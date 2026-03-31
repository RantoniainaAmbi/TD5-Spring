package com.td2.td5spring.controller;

import com.td2.td5spring.entity.Ingredient;
import com.td2.td5spring.entity.StockMovement;
import com.td2.td5spring.entity.StockValue;
import com.td2.td5spring.repository.IngredientRepository;
import com.td2.td5spring.repository.StockMovementRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {
    private final IngredientRepository ingredientRepository;
    private final StockMovementRepository stockMovementRepository;

    public IngredientController(IngredientRepository ingredientRepository, StockMovementRepository stockMovementRepository) {
        this.ingredientRepository = ingredientRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

    @GetMapping
    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    @GetMapping("/{id}")
    public Ingredient getIngredientById(@PathVariable int id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Ingredient.id id=" + id + " is not found"));
    }

    @GetMapping("/{id}/stock")
    public StockValue getStock(
            @PathVariable int id,
            @RequestParam(required = false) String at,
            @RequestParam(required = false) String unit) {

        if (at == null || unit == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Either mandatory query parameter at or unit is not provided.");
        }

        ingredientRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Ingredient.id id=" + id + " is not found"));

        return ingredientRepository.getStockValue(id, Instant.from(LocalDateTime.parse(at)), unit);
    }

    @GetMapping("/{id}/stockMovements")
    public List<StockMovement> getStockMovements(
            @PathVariable int id,
            @RequestParam Instant from,
            @RequestParam Instant to) {

        ingredientRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Ingredient.id=" + id + " is not found"
                ));

        return stockMovementRepository.findByIngredientIdAndDateRange(id, from, to);
    }
}