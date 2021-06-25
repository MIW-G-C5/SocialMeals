package nl.miwgroningen.cohort5.socialmeals.service;

import nl.miwgroningen.cohort5.socialmeals.dto.IngredientDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.IngredientRecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.model.IngredientRecipe;
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

    void updateRecipe(RecipeDTO oldRecipeDTO, RecipeDTO updatedRecipeDTO);

    RecipeDTO deleteRecipe(RecipeDTO recipeDTO);

    RecipeDTO findByUrlId(Long urlId);

    void addIngredientsToRecipe(List<IngredientRecipeDTO> ingredientRecipeList);

    void addIngredientToRecipe(IngredientRecipeDTO ingredientRecipeDTO);

    void deleteIngredientFromRecipe(IngredientRecipe ingredientRecipe);

    IngredientRecipe getIngredientRecipeByNameAndUrlId(String ingredientName, Long urlId);

    List<IngredientRecipeDTO> getIngredientRecipesByRecipeUrlId(Long urlId);

    List<IngredientDTO> getRemainingIngredientsByUrlId(Long urlId);

    List<RecipeDTO> getRecipesByUsername(String username);

    Recipe getRecipeByRecipeDTO(RecipeDTO recipeDTO);

    List<Long> search(String keyword);
}
