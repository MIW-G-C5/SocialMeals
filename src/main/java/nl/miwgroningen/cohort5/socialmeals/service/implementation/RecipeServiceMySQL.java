package nl.miwgroningen.cohort5.socialmeals.service.implementation;

import nl.miwgroningen.cohort5.socialmeals.dto.IngredientRecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.model.IngredientRecipe;
import nl.miwgroningen.cohort5.socialmeals.model.Recipe;
import nl.miwgroningen.cohort5.socialmeals.model.SocialMealsUser;
import nl.miwgroningen.cohort5.socialmeals.repository.IngredientRecipeRepository;
import nl.miwgroningen.cohort5.socialmeals.repository.IngredientRepository;
import nl.miwgroningen.cohort5.socialmeals.repository.RecipeRepository;
import nl.miwgroningen.cohort5.socialmeals.repository.SocialMealsUserRepository;
import nl.miwgroningen.cohort5.socialmeals.service.RecipeService;
import nl.miwgroningen.cohort5.socialmeals.service.dtoconverter.IngredientRecipeConverter;
import nl.miwgroningen.cohort5.socialmeals.service.dtoconverter.RecipeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * @author A.H. van Zessen
 */

@Service
public class RecipeServiceMySQL implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final IngredientRecipeRepository ingredientRecipeRepository;
    private SocialMealsUserRepository socialMealsUserRepository;

    private RecipeConverter recipeConverter;
    private IngredientRecipeConverter ingredientRecipeConverter;

    @Autowired
    public RecipeServiceMySQL(RecipeRepository recipeRepository,
                              IngredientRecipeRepository ingredientRecipeRepository,
                              IngredientRepository ingredientRepository,
                              SocialMealsUserRepository socialMealsUserRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRecipeRepository = ingredientRecipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.socialMealsUserRepository = socialMealsUserRepository;

        recipeConverter = new RecipeConverter(recipeRepository, socialMealsUserRepository);
        ingredientRecipeConverter =
                new IngredientRecipeConverter(recipeRepository, ingredientRepository, socialMealsUserRepository);
    }

    @Override
    public List<RecipeDTO> getAll() {
        List<Recipe> recipeList = recipeRepository.findAll();
        return recipeConverter.toListDTO(recipeList);
    }

    @Override
    public RecipeDTO addNew(RecipeDTO recipeDTO) {
        Recipe recipe = recipeConverter.fromDTO(recipeDTO);
        recipeRepository.save(recipe);
        return recipeDTO;
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
            ingredientRecipeRepository.save(ingredientRecipeConverter.fromDTO(ingredientRecipeDTO));
        }
    }

    @Override
    public List<IngredientRecipeDTO> getIngredientRecipesByRecipeName(String recipeName){

        Optional<Recipe> recipe = recipeRepository.findByRecipeName(recipeName);

        if(recipe.isEmpty()){
           return null;
        }

        List<IngredientRecipe> ingredientRecipeList = ingredientRecipeRepository.findIngredientRecipeByRecipe(recipe.get());
        List<IngredientRecipeDTO> ingredientRecipeDTOList = ingredientRecipeConverter.toListDTO(ingredientRecipeList);

        return ingredientRecipeDTOList;
    }

    @Override
    public List<RecipeDTO> getRecipesByUsername(String username){
        Optional<SocialMealsUser> user = socialMealsUserRepository.findByUsername(username);
        List<Recipe> recipes = recipeRepository.findRecipesBySocialMealsUser(user.get());

        if(user.isEmpty() || recipes.isEmpty()){
            return null;
        }

        return recipeConverter.toListDTO(recipes);
    }

}
