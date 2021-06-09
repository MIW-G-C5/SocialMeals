package nl.miwgroningen.cohort5.socialmeals.dto;

import java.util.Objects;

/**
 * Britt van Mourik
 *
 * Describes the details of an IngredientDTO
 */

public class IngredientDTO {

    private String ingredientName;

    public IngredientDTO(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientDTO that = (IngredientDTO) o;
        return ingredientName.equals(that.ingredientName);
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }
}
