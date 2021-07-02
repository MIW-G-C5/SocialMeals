package nl.miwgroningen.cohort5.socialmeals.dto;


import javax.persistence.Lob;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author A.H. van Zessen
 *
 * Describes the details of a RecipeDTO
 */

public class RecipeDTO {

    private Long urlId;
    private String recipeName;
    private List<String> steps;
    private byte[] recipeImage;
    private SocialMealsUserDTO socialMealsUserDTO;
    private String averageRating;
    private int numberOfRatings;

    public RecipeDTO(String recipeName, List<String> steps, SocialMealsUserDTO socialMealsUserDTO) {
        this.recipeName = recipeName;
        this.steps = steps;
        this.socialMealsUserDTO = socialMealsUserDTO;
    }

    public RecipeDTO() {
        this.steps = new ArrayList<>();
    }

    public byte[] getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(byte[] recipeImage) {
        this.recipeImage = recipeImage;
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

    public Long getUrlId() {
        return urlId;
    }

    public void setUrlId(Long urlId) {
        this.urlId = urlId;
    }

    public String getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(String averageRating) {
        this.averageRating = averageRating;
    }

    public int getNumberOfRatings() {
        return numberOfRatings;
    }

    public void setNumberOfRatings(int numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }
}
