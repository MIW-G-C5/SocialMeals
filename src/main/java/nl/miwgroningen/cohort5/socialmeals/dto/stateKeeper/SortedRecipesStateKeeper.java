package nl.miwgroningen.cohort5.socialmeals.dto.stateKeeper;

import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Britt van Mourik
 * saves sorted recipes in a session
 */

public class SortedRecipesStateKeeper {

    private List<RecipeDTO> sortedRecipes;

    public SortedRecipesStateKeeper() {
        this.sortedRecipes = new ArrayList<>();
    }

    public List<RecipeDTO> getSortedRecipes() {
        return sortedRecipes;
    }

    public void setSortedRecipes(List<RecipeDTO> sortedRecipes) {
        this.sortedRecipes = sortedRecipes;
    }
}
