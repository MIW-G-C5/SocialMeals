package nl.miwgroningen.cohort5.socialmeals.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author A.H. van Zessen
 */

public class RecipeDTO {
    private String recipeName;
    private List<String> steps;
    private SocialMealsUserDTO socialMealsUserDTO;

    public RecipeDTO(String recipeName, List<String> steps, SocialMealsUserDTO socialMealsUserDTO) {
        this.recipeName = recipeName;
        this.steps = steps;
        this.socialMealsUserDTO = socialMealsUserDTO;
    }

    public RecipeDTO() {
    }

    public SocialMealsUserDTO getSocialMealsUserDTO() {
        return socialMealsUserDTO;
    }

    public void setSocialMealsUserDTO(SocialMealsUserDTO socialMealsUserDTO) {
        this.socialMealsUserDTO = socialMealsUserDTO;
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

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }
}
