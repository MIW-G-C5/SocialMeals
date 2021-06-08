package nl.miwgroningen.cohort5.socialmeals.service.implementation;

import nl.miwgroningen.cohort5.socialmeals.dto.IngredientRecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.model.IngredientRecipe;
import nl.miwgroningen.cohort5.socialmeals.model.Recipe;
import nl.miwgroningen.cohort5.socialmeals.model.SocialMealsUser;
import nl.miwgroningen.cohort5.socialmeals.repository.IngredientRecipeRepository;
import nl.miwgroningen.cohort5.socialmeals.repository.RecipeRepository;
import nl.miwgroningen.cohort5.socialmeals.repository.SocialMealsUserRepository;
import nl.miwgroningen.cohort5.socialmeals.service.IngredientService;
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
 *
 * Collects and stores Recipes in the MySQL Database.
 */

@Service
public class RecipeServiceMySQL implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRecipeRepository ingredientRecipeRepository;
    private SocialMealsUserRepository socialMealsUserRepository;

    private IngredientService ingredientService;
    private SocialMealsUserDetailService socialMealsUserDetailService;

    private RecipeConverter recipeConverter;
    private IngredientRecipeConverter ingredientRecipeConverter;

    @Autowired
    public RecipeServiceMySQL(RecipeRepository recipeRepository,
                              IngredientRecipeRepository ingredientRecipeRepository,
                              SocialMealsUserRepository socialMealsUserRepository,

                              IngredientService ingredientService,
                              SocialMealsUserDetailService socialMealsUserDetailService) {
        this.recipeRepository = recipeRepository;
        this.ingredientRecipeRepository = ingredientRecipeRepository;
        this.socialMealsUserRepository = socialMealsUserRepository;


        this.ingredientService = ingredientService;
        this.socialMealsUserDetailService = socialMealsUserDetailService;

        recipeConverter = new RecipeConverter(socialMealsUserDetailService);
        ingredientRecipeConverter =
                new IngredientRecipeConverter(this, ingredientService, socialMealsUserDetailService);
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
    public void updateRecipe(RecipeDTO oldRecipeDTO, RecipeDTO updatedRecipeDTO) {
        Recipe oldRecipe = getRecipeByRecipeDTO(oldRecipeDTO);
        Recipe newRecipe = recipeConverter.fromDTO(oldRecipe, updatedRecipeDTO);

        recipeRepository.save(newRecipe);
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
    public void addIngredientsToRecipe(List<IngredientRecipeDTO> ingredientRecipeDTOS) {
        for (IngredientRecipeDTO ingredientRecipeDTO : ingredientRecipeDTOS) {
            ingredientRecipeRepository.save(ingredientRecipeConverter.fromDTO(ingredientRecipeDTO));
        }
    }

    @Override
    public void addIngredientToRecipe(IngredientRecipeDTO ingredientRecipeDTO) {
        ingredientRecipeRepository.save(ingredientRecipeConverter.fromDTO(ingredientRecipeDTO));
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
        List<Recipe> recipes = null;

        if(user.isPresent()){
            recipes = recipeRepository.findRecipesBySocialMealsUser(user.get());
        }

        return recipeConverter.toListDTO(recipes);
    }

    public Recipe getRecipeByRecipeDTO(RecipeDTO recipeDTO) {
        Optional<Recipe> recipe = recipeRepository.findByRecipeName(recipeDTO.getRecipeName());
        if(recipe.isPresent()){
            return recipe.get();
        } else {
            return null;
        }
    }

}
