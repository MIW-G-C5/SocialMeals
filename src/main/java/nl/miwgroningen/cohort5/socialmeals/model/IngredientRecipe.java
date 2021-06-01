package nl.miwgroningen.cohort5.socialmeals.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Britt van Mourik
 */

@Entity
public class IngredientRecipe {

    @EmbeddedId
    IngredientRecipeKey id;

    @ManyToOne
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id")
    Ingredient ingredient;

    @ManyToOne
    @MapsId("recipeId")
    @JoinColumn(name = "recipe_id")
    Recipe recipe;

    double quantity;
    String quantityType;

    public Ingredient getIngredient() {
        return ingredient;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getQuantityType() {
        return quantityType;
    }
}
