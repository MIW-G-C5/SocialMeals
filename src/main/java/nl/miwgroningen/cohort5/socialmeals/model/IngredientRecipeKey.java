package nl.miwgroningen.cohort5.socialmeals.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Britt van Mourik
 */

@Embeddable
public class IngredientRecipeKey implements Serializable {

    @Column(name = "ingredient_id")
    Long ingredientId;

    @Column(name = "recipe_id")
    Long recipeId;

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientRecipeKey that = (IngredientRecipeKey) o;
        return ingredientId.equals(that.ingredientId) && recipeId.equals(that.recipeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredientId, recipeId);
    }
}