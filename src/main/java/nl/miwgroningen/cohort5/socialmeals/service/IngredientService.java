package nl.miwgroningen.cohort5.socialmeals.service;

import nl.miwgroningen.cohort5.socialmeals.dto.IngredientDTO;
import nl.miwgroningen.cohort5.socialmeals.model.Ingredient;


import java.util.List;

public interface IngredientService {

    List<IngredientDTO> getAll();

    Ingredient addNew(Ingredient ingredient);

    IngredientDTO findByIngredientName(String ingredientName);

}
