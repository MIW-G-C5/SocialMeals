package nl.miwgroningen.cohort5.socialmeals.service.dtoconverter;

import nl.miwgroningen.cohort5.socialmeals.dto.IngredientDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.model.Ingredient;
import nl.miwgroningen.cohort5.socialmeals.model.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Britt van Mourik
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
