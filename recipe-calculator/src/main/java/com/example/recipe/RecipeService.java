package com.example.recipe;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class RecipeService {

    private static final Logger LOG = Logger.getLogger(RecipeService.class);
    private static final String INDEX_PATH = "recipes/index.json";

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final Map<String, Recipe> recipes = new HashMap<>();

    @PostConstruct
    void loadRecipes() {
        try (InputStream indexStream = getResourceStream(INDEX_PATH)) {
            if (indexStream == null) {
                throw new IllegalStateException("Missing recipe index file: " + INDEX_PATH);
            }
            List<String> files = objectMapper.readValue(indexStream, new TypeReference<>() {
            });
            for (String file : files) {
                loadRecipeFile(file);
            }
            LOG.infov("Loaded {0} recipes", recipes.size());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load recipes", e);
        }
    }

    public List<Recipe> getAllRecipes() {
        return recipes.values().stream()
                .sorted(Comparator.comparing(Recipe::name))
                .toList();
    }

    public ScaledRecipe scaleRecipe(String id, double requestedWeightKg) {
        Recipe recipe = recipes.get(id);
        if (recipe == null) {
            throw new WebApplicationException("Recipe not found", Response.Status.NOT_FOUND);
        }
        if (requestedWeightKg <= 0) {
            throw new WebApplicationException("Weight must be greater than zero", Response.Status.BAD_REQUEST);
        }

        double factor = requestedWeightKg / recipe.baseWeightKg();
        List<Ingredient> scaled = new ArrayList<>();
        for (Ingredient ingredient : recipe.ingredients()) {
            double amount = Math.round(ingredient.quantity() * factor * 100.0) / 100.0;
            scaled.add(new Ingredient(ingredient.name(), amount, ingredient.unit()));
        }

        return new ScaledRecipe(recipe.id(), recipe.name(), recipe.baseWeightKg(), requestedWeightKg, scaled);
    }

    private void loadRecipeFile(String file) throws IOException {
        String path = "recipes/" + file;
        try (InputStream stream = getResourceStream(path)) {
            if (stream == null) {
                LOG.warnf("Skipping missing recipe file: %s", path);
                return;
            }
            Recipe recipe = objectMapper.readValue(stream, Recipe.class);
            recipes.put(recipe.id(), recipe);
        }
    }

    private InputStream getResourceStream(String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }
}
