package nl.miwgroningen.cohort5.socialmeals.service.dtoconverter;

import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.model.Recipe;
import nl.miwgroningen.cohort5.socialmeals.repository.RecipeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author A.H. van Zessen
 */

public class RecipeConverter {

    private RecipeRepository recipeRepository;

    public RecipeConverter(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public RecipeDTO toDTO(Recipe recipe) {
        return new RecipeDTO(recipe.getRecipeName(), recipe.getSteps());
    }

    public List<RecipeDTO> toListDTO(List<Recipe> recipeList) {
        List<RecipeDTO> returnList = new ArrayList<>();

        for (Recipe recipe : recipeList) {
            returnList.add(toDTO(recipe));
        }

        return returnList;
    }

    public Recipe fromDTO(RecipeDTO recipeDTO) {
        return new Recipe(recipeDTO.getRecipeName(), recipeDTO.getSteps());
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
