package nl.miwgroningen.cohort5.socialmeals.service.dtoconverter;

import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.model.Recipe;
import nl.miwgroningen.cohort5.socialmeals.repository.RecipeRepository;
import nl.miwgroningen.cohort5.socialmeals.repository.SocialMealsUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author A.H. van Zessen
 */

public class RecipeConverter {

    private RecipeRepository recipeRepository;
    private SocialMealsUserRepository socialMealsUserRepository;

    private SocialMealsUserConverter socialMealsUserConverter;

    public RecipeConverter(RecipeRepository recipeRepository, SocialMealsUserRepository socialMealsUserRepository) {
        this.recipeRepository = recipeRepository;
        this.socialMealsUserRepository = socialMealsUserRepository;
        socialMealsUserConverter = new SocialMealsUserConverter(socialMealsUserRepository);
    }

    public RecipeDTO toDTO(Recipe recipe) {
        return new RecipeDTO(recipe.getRecipeName(), recipe.getSteps(),
                socialMealsUserConverter.toDTO(recipe.getSocialMealsUser()));
    }

    public List<RecipeDTO> toListDTO(List<Recipe> recipeList) {
        List<RecipeDTO> returnList = new ArrayList<>();

        for (Recipe recipe : recipeList) {
            returnList.add(toDTO(recipe));
        }

        return returnList;
    }

    public Recipe fromDTO(RecipeDTO recipeDTO) {
        return new Recipe(recipeDTO.getRecipeName(), recipeDTO.getSteps(),
                socialMealsUserConverter.fromDTO(recipeDTO.getSocialMealsUserDTO()));
    }

    public Recipe fromDTOToDatabaseRecipe(RecipeDTO recipeDTO){

            Optional<Recipe> recipe = recipeRepository.findByRecipeName(recipeDTO.getRecipeName());
            if(recipe.isPresent()){
                return recipe.get();
            } else {
                return null;
            }

    }
}
