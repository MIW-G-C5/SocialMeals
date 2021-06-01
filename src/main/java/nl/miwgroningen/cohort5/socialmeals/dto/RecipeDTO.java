package nl.miwgroningen.cohort5.socialmeals.dto;

/**
 * @author A.H. van Zessen
 */

public class RecipeDTO {
    private String recipeName;
    private String description;

    public RecipeDTO(String recipeName, String description) {
        this.recipeName = recipeName;
        this.description = description;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
