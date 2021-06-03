package nl.miwgroningen.cohort5.socialmeals.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author A.H. van Zessen
 */

public class RecipeDTO {
    private String recipeName;
    private List<String> steps;

    public RecipeDTO(String recipeName, List<String> steps) {
        this.recipeName = recipeName;
        this.steps = steps;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<String> steps) {
        this.steps = steps;
    }
}
