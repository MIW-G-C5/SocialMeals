package nl.miwgroningen.cohort5.socialmeals.service.dtoconverter;

import nl.miwgroningen.cohort5.socialmeals.dto.IngredientRecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.model.Ingredient;
import nl.miwgroningen.cohort5.socialmeals.model.IngredientRecipe;
import nl.miwgroningen.cohort5.socialmeals.model.Recipe;
import nl.miwgroningen.cohort5.socialmeals.repository.IngredientRepository;
import nl.miwgroningen.cohort5.socialmeals.repository.RecipeRepository;

import javax.persistence.criteria.CriteriaBuilder;

/**
 * Britt van Mourik
 */

public class IngredientRecipeConverter {

    private RecipeRepository recipeRepository;
    private IngredientRepository ingredientRepository;

    private RecipeConverter recipeConverter;
    private IngredientConverter ingredientConverter;

    public IngredientRecipeConverter(RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        recipeConverter = new RecipeConverter(recipeRepository);
        ingredientConverter = new IngredientConverter(ingredientRepository);
    }

    public IngredientRecipe fromDTO(IngredientRecipeDTO ingredientRecipeDTO){

        Recipe recipe = recipeConverter.fromDTOToDatabaseRecipe(ingredientRecipeDTO.getRecipeDTO());
        Ingredient ingredient = ingredientConverter.fromDTOToDatabaseIngredient(ingredientRecipeDTO.getIngredientDTO());

        if (recipe == null || ingredient == null) {
            return null;
        }

        IngredientRecipe ingredientRecipe = new IngredientRecipe(
                ingredient,
                recipe,
                ingredientRecipeDTO.getQuantity(),
                ingredientRecipeDTO.getQuantityType()
        );

        return ingredientRecipe;
    }
}
