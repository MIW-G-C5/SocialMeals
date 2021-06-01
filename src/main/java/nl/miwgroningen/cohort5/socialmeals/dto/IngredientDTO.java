package nl.miwgroningen.cohort5.socialmeals.dto;

/**
 * Britt van Mourik
 */

public class IngredientDTO {

    private String ingredientName;

    public IngredientDTO(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }
}
