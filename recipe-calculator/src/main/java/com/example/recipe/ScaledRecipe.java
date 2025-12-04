package com.example.recipe;

import java.util.List;

public record ScaledRecipe(String id,
                           String name,
                           double baseWeightKg,
                           double requestedWeightKg,
                           List<Ingredient> ingredients) {
}
