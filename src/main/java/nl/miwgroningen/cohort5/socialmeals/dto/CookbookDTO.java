package nl.miwgroningen.cohort5.socialmeals.dto;


import java.util.ArrayList;
import java.util.List;

/**
 * Britt van Mourik
 * Describes the details of an CookbookDTO
 */

public class CookbookDTO {

    private Long urlId;
    private String cookbookName;
    private SocialMealsUserDTO socialMealsUserDTO;
    private List<RecipeDTO> recipes;

    public CookbookDTO(Long urlId, String cookbookName, SocialMealsUserDTO socialMealsUserDTO, List<RecipeDTO> recipes) {
        this(cookbookName, socialMealsUserDTO, recipes);
        this.urlId = urlId;
    }

    public CookbookDTO(String cookbookName, SocialMealsUserDTO socialMealsUserDTO, List<RecipeDTO> recipes) {
        this.cookbookName = cookbookName;
        this.socialMealsUserDTO = socialMealsUserDTO;
        this.recipes = recipes;
    }

    public CookbookDTO(){
        this.recipes = new ArrayList<>();
    }

    public String getCookbookName() {
        return cookbookName;
    }

    public void setCookbookName(String cookbookName) {
        this.cookbookName = cookbookName;
    }

    public SocialMealsUserDTO getSocialMealsUser() {
        return socialMealsUserDTO;
    }

    public void setSocialMealsUser(SocialMealsUserDTO socialMealsUserDTO) {
        this.socialMealsUserDTO = socialMealsUserDTO;
    }

    public List<RecipeDTO> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<RecipeDTO> recipes) {
        this.recipes = recipes;
    }

    public Long getUrlId() {
        return urlId;
    }

    public void setUrlId(Long urlId) {
        this.urlId = urlId;
    }
}
