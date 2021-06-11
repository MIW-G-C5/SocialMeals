package nl.miwgroningen.cohort5.socialmeals.comparator;

import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;

import java.util.Comparator;

/**
 * Britt van Mourik
 */

public class RecipeDTOComparator implements Comparator<RecipeDTO> {


    @Override
    public int compare(RecipeDTO recipeDTO, RecipeDTO otherRecipeDTO){
        return recipeDTO.getRecipeName().compareTo(otherRecipeDTO.getRecipeName());
    }

}
