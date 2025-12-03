package com.example.recipe;

import java.util.List;

public record Recipe(String id, String name, double baseWeightKg, List<Ingredient> ingredients,String book, String edition, String page) {
}
