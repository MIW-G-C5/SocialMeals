package nl.miwgroningen.cohort5.socialmeals.comparator;

import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;

import java.util.Comparator;

/**
 * Britt van Mourik
 * sorts RecipeDTOs in descending order
 */

public class RecipeDTODescComparator implements Comparator<RecipeDTO> {

    @Override
    public int compare(RecipeDTO recipeDTO, RecipeDTO otherRecipeDTO) {
        return otherRecipeDTO.getRecipeName().compareTo(recipeDTO.getRecipeName());
    }

}
