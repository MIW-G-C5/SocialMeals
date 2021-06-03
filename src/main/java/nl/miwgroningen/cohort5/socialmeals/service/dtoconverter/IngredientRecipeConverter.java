package nl.miwgroningen.cohort5.socialmeals.service.dtoconverter;

import nl.miwgroningen.cohort5.socialmeals.dto.IngredientRecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.model.Ingredient;
import nl.miwgroningen.cohort5.socialmeals.model.IngredientRecipe;
import nl.miwgroningen.cohort5.socialmeals.model.Recipe;
import nl.miwgroningen.cohort5.socialmeals.repository.IngredientRepository;
import nl.miwgroningen.cohort5.socialmeals.repository.RecipeRepository;
import nl.miwgroningen.cohort5.socialmeals.repository.SocialMealsUserRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Britt van Mourik
 */

public class IngredientRecipeConverter {

    private RecipeRepository recipeRepository;
    private IngredientRepository ingredientRepository;
    private SocialMealsUserRepository socialMealsUserRepository;

    private RecipeConverter recipeConverter;
    private IngredientConverter ingredientConverter;

    public IngredientRecipeConverter(RecipeRepository recipeRepository,
                                     IngredientRepository ingredientRepository,
                                     SocialMealsUserRepository socialMealsUserRepository) {

        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.socialMealsUserRepository = socialMealsUserRepository;

        recipeConverter = new RecipeConverter(recipeRepository, socialMealsUserRepository);
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

    public IngredientRecipeDTO toDTO(IngredientRecipe ingredientRecipe){

        return new IngredientRecipeDTO(
                ingredientConverter.toDTO(ingredientRecipe.getIngredient()),
                recipeConverter.toDTO(ingredientRecipe.getRecipe()),
                ingredientRecipe.getQuantity(),
                ingredientRecipe.getQuantityType());

    }

    public List<IngredientRecipeDTO> toListDTO(List<IngredientRecipe> ingredientRecipeList){
       List<IngredientRecipeDTO> ingredientRecipeDTOList = new ArrayList<>();

        for (IngredientRecipe ingredientRecipe : ingredientRecipeList) {
            ingredientRecipeDTOList.add(toDTO(ingredientRecipe));
        }

        return ingredientRecipeDTOList;
    }
}
