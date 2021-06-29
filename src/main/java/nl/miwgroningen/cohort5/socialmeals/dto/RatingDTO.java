package nl.miwgroningen.cohort5.socialmeals.dto;

/**
 * @author Wessel van Dommelen <w.r.van.dommelen@st.hanze.nl>
 *
 *     Describes the properties of the data transfer object of a Rating.
 */
public class RatingDTO {

    private int forks;
    private RecipeDTO recipeDTO;
    private SocialMealsUserDTO socialMealsUserDTO;

    public RatingDTO(int forks, RecipeDTO recipeDTO, SocialMealsUserDTO socialMealsUserDTO) {
        this.forks = forks;
        this.recipeDTO = recipeDTO;
        this.socialMealsUserDTO = socialMealsUserDTO;
    }

    public RatingDTO() {
    }

    public int getForks() {
        return forks;
    }

    public void setForks(int forks) {
        this.forks = forks;
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
