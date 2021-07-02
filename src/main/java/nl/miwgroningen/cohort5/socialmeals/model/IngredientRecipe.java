package nl.miwgroningen.cohort5.socialmeals.model;

import javax.persistence.*;

/**
 * Britt van Mourik
 */

@Entity
public class IngredientRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long ingredientRecipeId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ingredient_id")
    Ingredient ingredient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recipe_id")
    Recipe recipe;

    double quantity;
    String quantityType;

    public IngredientRecipe(Ingredient ingredient, Recipe recipe, double quantity, String quantityType) {
        this.ingredient = ingredient;
        this.recipe = recipe;
        this.quantity = quantity;
        this.quantityType = quantityType;
    }

    public IngredientRecipe() {
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setQuantityType(String quantityType) {
        this.quantityType = quantityType;
    }

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

    public Long getIngredientRecipeId() {
        return ingredientRecipeId;
    }

    public void setIngredientRecipeId(Long ingredientRecipeId) {
        this.ingredientRecipeId = ingredientRecipeId;
    }
}
