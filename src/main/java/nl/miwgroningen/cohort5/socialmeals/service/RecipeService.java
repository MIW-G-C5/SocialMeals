package nl.miwgroningen.cohort5.socialmeals.service;

import nl.miwgroningen.cohort5.socialmeals.dto.IngredientDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.IngredientRecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.model.IngredientRecipe;
import nl.miwgroningen.cohort5.socialmeals.model.Recipe;

import java.util.List;

/**
 * @author A.H. van Zessen
 */

public interface RecipeService {
    List<RecipeDTO> getAll();

    RecipeDTO addNew(RecipeDTO recipeDTO);

    RecipeDTO findByRecipeName(String recipeName);

    void addIngredientsToRecipe(List<IngredientRecipeDTO> ingredientRecipeList);

    List<IngredientRecipeDTO> getIngredientRecipesByRecipeName(String recipeName);

    List<RecipeDTO> getRecipesByUsername(String username);
}
