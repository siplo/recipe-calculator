package com.example.recipe;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
class RecipeResourceTest {

    @Test
    void shouldListRecipes() {
        given()
                .when().get("/api/recipes")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("", hasSize(3))
                .body("[0].id", notNullValue());
    }

    @Test
    void shouldScaleRecipe() {
        given()
                .queryParam("weightKg", 5)
                .when().get("/api/recipes/beef-goulash/scale")
                .then()
                .statusCode(200)
                .body("requestedWeightKg", equalTo(5.0f))
                .body("ingredients[0].quantity", equalTo(5.0f))
                .body("ingredients[2].name", containsString("Paprika"));
    }
}
