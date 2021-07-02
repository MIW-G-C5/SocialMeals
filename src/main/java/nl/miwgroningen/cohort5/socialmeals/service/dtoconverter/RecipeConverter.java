package nl.miwgroningen.cohort5.socialmeals.service.dtoconverter;

import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.model.Recipe;
import nl.miwgroningen.cohort5.socialmeals.model.SocialMealsUser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author A.H. van Zessen
 *
 * Converts Recipes into RecipeDTO's and vice versa
 */

public class RecipeConverter {

    private final SocialMealsUserConverter socialMealsUserConverter;

    public RecipeConverter() {
        socialMealsUserConverter = new SocialMealsUserConverter();
    }

    public RecipeDTO toDTO(Recipe recipe) {
        RecipeDTO recipeDTO = new RecipeDTO(recipe.getRecipeName(), recipe.getSteps(),
                socialMealsUserConverter.toDTO(recipe.getSocialMealsUser()));
        recipeDTO.setUrlId(recipe.getUrlId());
        recipeDTO.setRecipeImage(recipe.getRecipeImage());
        return recipeDTO;
    }

    public List<RecipeDTO> toListDTO(List<Recipe> recipeList) {
        List<RecipeDTO> returnList = new ArrayList<>();

        for (Recipe recipe : recipeList) {
            returnList.add(toDTO(recipe));
        }

        return returnList;
    }

    public Recipe fromDTO(RecipeDTO recipeDTO, SocialMealsUser socialMealsUser) {
        Recipe recipe = new Recipe(recipeDTO.getRecipeName(),
                recipeDTO.getSteps(),
                socialMealsUser);
        recipe.setUrlId(recipeDTO.getUrlId());

        return recipe;
    }

    public Recipe fromDTO(Recipe recipe, RecipeDTO recipeDTO) {
        recipe.setRecipeName(recipeDTO.getRecipeName());
        recipe.setSteps(recipeDTO.getSteps());
        return recipe;
    }

    public Recipe fromDTOWithImage(Recipe recipe, RecipeDTO recipeDTO) {
        recipe.setRecipeImage(recipeDTO.getRecipeImage());
        return recipe;
    }
}
