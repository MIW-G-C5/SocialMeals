package nl.miwgroningen.cohort5.socialmeals.dto;

/**
 * @author Wessel van Dommelen <w.r.van.dommelen@st.hanze.nl>
 *
 * Describes the properties of the data transfer object of a Rating.
 */
public class RatingDTO {

    private int stars;
    private RecipeDTO recipeDTO;
    private SocialMealsUserDTO socialMealsUserDTO;

    public RatingDTO(int stars, RecipeDTO recipeDTO, SocialMealsUserDTO socialMealsUserDTO) {
        this.stars = stars;
        this.recipeDTO = recipeDTO;
        this.socialMealsUserDTO = socialMealsUserDTO;
    }

    public RatingDTO() {
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public RecipeDTO getRecipeDTO() {
        return recipeDTO;
    }

    public void setRecipeDTO(RecipeDTO recipeDTO) {
        this.recipeDTO = recipeDTO;
    }

    public SocialMealsUserDTO getSocialMealsUserDTO() {
        return socialMealsUserDTO;
    }

    public void setSocialMealsUserDTO(SocialMealsUserDTO socialMealsUserDTO) {
        this.socialMealsUserDTO = socialMealsUserDTO;
    }
}
