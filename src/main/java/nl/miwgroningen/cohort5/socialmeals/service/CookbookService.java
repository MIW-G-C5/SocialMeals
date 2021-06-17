package nl.miwgroningen.cohort5.socialmeals.service;

import nl.miwgroningen.cohort5.socialmeals.dto.CookbookDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.IngredientDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.SocialMealsUserDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;

import java.util.List;

/**
 * Britt van Mourik
 * establishes the required functions for the connection to the Database for handling cookbooks.
 */

public interface CookbookService {

    CookbookDTO findByUrlId(Long urlId);

    List<CookbookDTO> getCookbooksByUser(SocialMealsUserDTO socialMealsUserDTO);

    CookbookDTO addNew(CookbookDTO cookbookDTO);

    CookbookDTO addRecipeDTO(CookbookDTO cookbookDTO, RecipeDTO recipeDTO);

    CookbookDTO removeRecipeDTO(CookbookDTO cookbookDTO, RecipeDTO recipeDTO);

}
