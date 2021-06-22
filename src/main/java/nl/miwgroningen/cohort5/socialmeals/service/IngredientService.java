package nl.miwgroningen.cohort5.socialmeals.service;

import nl.miwgroningen.cohort5.socialmeals.dto.IngredientDTO;
import nl.miwgroningen.cohort5.socialmeals.model.Ingredient;

import java.util.List;

/**
 * @author W.R. van Dommelen
 *
 * establishes the required functions for the connection to the Database for handling ingredients.
 */

public interface IngredientService {

    List<IngredientDTO> getAll();

    IngredientDTO addNew(IngredientDTO ingredientDTO);

    IngredientDTO findByIngredientName(String ingredientName);

    Ingredient getIngredientByIngredientDTO(IngredientDTO ingredientDTO);

    List<String> search(String keyword);
}
