package nl.miwgroningen.cohort5.socialmeals.service.dtoconverter;

import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.model.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * @author A.H. van Zessen
 */

public class RecipeConverter {
    public RecipeDTO toDTO(Recipe recipe) {
        return new RecipeDTO(recipe.getRecipeName(), recipe.getDescription());
    }

    public List<RecipeDTO> toListDTO(List<Recipe> recipeList) {
        List<RecipeDTO> returnList = new ArrayList<>();

        for (Recipe recipe : recipeList) {
            returnList.add(toDTO(recipe));
        }

        return returnList;
    }

    public Recipe fromDTO(RecipeDTO recipeDTO) {
        return new Recipe(recipeDTO.getRecipeName(), recipeDTO.getDescription());
    }
}
