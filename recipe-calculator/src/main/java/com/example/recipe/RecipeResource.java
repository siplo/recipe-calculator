package com.example.recipe;

import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/api/recipes")
@Produces(MediaType.APPLICATION_JSON)
public class RecipeResource {

    @Inject
    RecipeService recipeService;

    @GET
    public List<Recipe> list() {
        return recipeService.getAllRecipes();
    }

    @GET
    @Path("/{id}/scale")
    public ScaledRecipe scale(@PathParam("id") String id,
                              @QueryParam("weightKg") @DefaultValue("10") double weightKg) {
        return recipeService.scaleRecipe(id, weightKg);
    }
}
