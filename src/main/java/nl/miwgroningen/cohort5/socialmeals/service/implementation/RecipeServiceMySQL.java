package nl.miwgroningen.cohort5.socialmeals.service.implementation;

import nl.miwgroningen.cohort5.socialmeals.dto.IngredientRecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.model.Ingredient;
import nl.miwgroningen.cohort5.socialmeals.model.IngredientRecipe;
import nl.miwgroningen.cohort5.socialmeals.model.Recipe;
import nl.miwgroningen.cohort5.socialmeals.repository.IngredientRecipeRepository;
import nl.miwgroningen.cohort5.socialmeals.repository.IngredientRepository;
import nl.miwgroningen.cohort5.socialmeals.repository.RecipeRepository;
import nl.miwgroningen.cohort5.socialmeals.service.RecipeService;
import nl.miwgroningen.cohort5.socialmeals.service.dtoconverter.IngredientConverter;
import nl.miwgroningen.cohort5.socialmeals.service.dtoconverter.RecipeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author A.H. van Zessen
 */

@Service
public class RecipeServiceMySQL implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final IngredientRecipeRepository ingredientRecipeRepository;

    RecipeConverter recipeConverter = new RecipeConverter();
    IngredientConverter ingredientConverter = new IngredientConverter();

    @Autowired
    public RecipeServiceMySQL(RecipeRepository recipeRepository,
                              IngredientRecipeRepository ingredientRecipeRepository,
                              IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRecipeRepository = ingredientRecipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public List<RecipeDTO> getAll() {
        List<Recipe> recipeList = recipeRepository.findAll();
        return recipeConverter.toListDTO(recipeList);
    }

    @Override
    public Recipe addNew(Recipe recipe) {
        recipeRepository.save(recipe);
        return recipe;
    }

    @Override
    public RecipeDTO findByRecipeName(String recipeName) {
        Optional<Recipe> recipe = recipeRepository.findByRecipeName(recipeName);
        RecipeDTO recipeDTO = null;

        if (recipe.isPresent()) {
            recipeDTO = recipeConverter.toDTO(recipe.get());
        }

        return recipeDTO;
    }

    @Override
    @Transactional
    public void addIngredientsToRecipe(List<IngredientRecipeDTO> ingredientRecipeDTOS) {
        for (IngredientRecipeDTO ingredientRecipeDTO : ingredientRecipeDTOS) {

            Optional<Recipe> recipe =
                    recipeRepository.findByRecipeName(ingredientRecipeDTO.getRecipeDTO().getRecipeName());
            Optional<Ingredient> ingredient =
                    ingredientRepository.findByIngredientName(ingredientRecipeDTO.getIngredientDTO().getIngredientName());

            if (recipe.isPresent() && ingredient.isPresent()) {

                Recipe realRecipe = recipe.get();
                Ingredient realIngredient = ingredient.get();

                IngredientRecipe ingredientRecipe = new IngredientRecipe(
                        realIngredient,
                        realRecipe,
                        ingredientRecipeDTO.getQuantity(),
                        ingredientRecipeDTO.getQuantityType()
                );

                ingredientRecipeRepository.save(ingredientRecipe);
            }
        }
    }
}
