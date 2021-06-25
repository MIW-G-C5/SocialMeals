package nl.miwgroningen.cohort5.socialmeals.service.dtoconverter;

import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.model.Recipe;
import nl.miwgroningen.cohort5.socialmeals.model.SocialMealsUser;
import nl.miwgroningen.cohort5.socialmeals.repository.RecipeRepository;
import nl.miwgroningen.cohort5.socialmeals.repository.SocialMealsUserRepository;
import nl.miwgroningen.cohort5.socialmeals.service.implementation.SocialMealsUserDetailService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author A.H. van Zessen
 *
 * Converts Recipes into RecipeDTO's and vice versa
 */

public class RecipeConverter {

    private SocialMealsUserConverter socialMealsUserConverter;

    public RecipeConverter() {
        socialMealsUserConverter = new SocialMealsUserConverter();
    }

    public RecipeDTO toDTO(Recipe recipe) {
        return new RecipeDTO(recipe.getRecipeName(), recipe.getSteps(),
                socialMealsUserConverter.toDTO(recipe.getSocialMealsUser()));
    }

    public List<RecipeDTO> toListDTO(List<Recipe> recipeList) {
        List<RecipeDTO> returnList = new ArrayList<>();

        for (Recipe recipe : recipeList) {
            returnList.add(toDTO(recipe));
        }

        return returnList;
    }

    public Recipe fromDTO(RecipeDTO recipeDTO, SocialMealsUser socialMealsUser) {
        return new Recipe(recipeDTO.getRecipeName(),
                recipeDTO.getSteps(),
                socialMealsUser);
    }

    public Recipe fromDTO(Recipe recipe, RecipeDTO recipeDTO) {
        recipe.setRecipeName(recipeDTO.getRecipeName());
        recipe.setSteps(recipeDTO.getSteps());
        return recipe;
    }
}
