package nl.miwgroningen.cohort5.socialmeals.service;

import nl.miwgroningen.cohort5.socialmeals.dto.IngredientRecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.model.Recipe;

import java.util.List;

/**
 * @author A.H. van Zessen
 *
 * establishes the required functions for the connection to the Database for handling recipes.
 */

public interface RecipeService {
    List<RecipeDTO> getAll();

    RecipeDTO addNew(RecipeDTO recipeDTO);

    RecipeDTO findByRecipeName(String recipeName);

    void addIngredientsToRecipe(List<IngredientRecipeDTO> ingredientRecipeList);

    List<IngredientRecipeDTO> getIngredientRecipesByRecipeName(String recipeName);

    List<RecipeDTO> getRecipesByUsername(String username);

    Recipe getRecipeByRecipeDTO(RecipeDTO recipeDTO);
}