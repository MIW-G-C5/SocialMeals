package nl.miwgroningen.cohort5.socialmeals.service.dtoconverter;

import nl.miwgroningen.cohort5.socialmeals.dto.IngredientRecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.model.Ingredient;
import nl.miwgroningen.cohort5.socialmeals.model.IngredientRecipe;
import nl.miwgroningen.cohort5.socialmeals.model.Recipe;
import nl.miwgroningen.cohort5.socialmeals.repository.IngredientRepository;
import nl.miwgroningen.cohort5.socialmeals.repository.RecipeRepository;
import nl.miwgroningen.cohort5.socialmeals.repository.SocialMealsUserRepository;
import nl.miwgroningen.cohort5.socialmeals.service.IngredientService;
import nl.miwgroningen.cohort5.socialmeals.service.RecipeService;
import nl.miwgroningen.cohort5.socialmeals.service.implementation.SocialMealsUserDetailService;

import java.util.ArrayList;
import java.util.List;

/**
 * Britt van Mourik
 *
 * Converts IngredientRecipes into IngredientRecipeDTO's and vice versa
 */

public class IngredientRecipeConverter {

    private RecipeService recipeService;
    private IngredientService ingredientService;
    private SocialMealsUserDetailService socialMealsUserDetailService;

    private RecipeConverter recipeConverter;
    private IngredientConverter ingredientConverter;

    public IngredientRecipeConverter(RecipeService recipeService,
                                     IngredientService ingredientService,
                                     SocialMealsUserDetailService socialMealsUserDetailService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.socialMealsUserDetailService = socialMealsUserDetailService;

        ingredientConverter = new IngredientConverter();
        recipeConverter = new RecipeConverter(socialMealsUserDetailService);
    }

    public IngredientRecipe fromDTO(IngredientRecipeDTO ingredientRecipeDTO){

        Recipe recipe = recipeService.getRecipeByRecipeDTO(ingredientRecipeDTO.getRecipeDTO());
        Ingredient ingredient = ingredientService.getIngredientByIngredientDTO(ingredientRecipeDTO.getIngredientDTO());

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
