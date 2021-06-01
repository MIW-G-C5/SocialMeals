package nl.miwgroningen.cohort5.socialmeals.service;

import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.model.Recipe;

import java.util.List;

/**
 * @author A.H. van Zessen
 */

public interface RecipeService {
    List<RecipeDTO> getAll();

    Recipe addNew(Recipe recipe);

    RecipeDTO findByRecipeName(String recipeName);
}
