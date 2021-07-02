package nl.miwgroningen.cohort5.socialmeals.service.dtoconverter;

import nl.miwgroningen.cohort5.socialmeals.dto.IngredientDTO;
import nl.miwgroningen.cohort5.socialmeals.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

/**
 * Britt van Mourik
 *
 * Converts Ingredients to IngredientDTO's and vice versa
 */

public class IngredientConverter {

    public IngredientDTO toDTO(Ingredient ingredient) {
        return new IngredientDTO(ingredient.getIngredientName());
    }

    public List<IngredientDTO> toListDTO(List<Ingredient> ingredientList) {
        List<IngredientDTO> returnList = new ArrayList<>();

        for (Ingredient ingredient : ingredientList) {
            returnList.add(toDTO(ingredient));
        }

        return returnList;
    }

    public Ingredient fromDTO(IngredientDTO ingredientDTO) {
        return new Ingredient(ingredientDTO.getIngredientName());
    }
}
