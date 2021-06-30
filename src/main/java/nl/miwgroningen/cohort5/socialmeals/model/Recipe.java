package nl.miwgroningen.cohort5.socialmeals.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Wessel van Dommelen <w.r.van.dommelen@st.hanze.nl>
 *
 *     describes the details of a Recipe
 */
@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeId;

    private Long urlId;

    @Column(nullable = false)
    private String recipeName;

    @ElementCollection
    @Column(length = 5000)
    private List<String> steps = new ArrayList<>();

    @Lob
    private byte[] recipeImage;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<IngredientRecipe> ingredientRecipes;

    @ManyToOne
    private SocialMealsUser socialMealsUser;

    @ManyToMany(mappedBy = "recipeLikes", cascade = CascadeType.ALL)
    Set<Cookbook> cookbookLikes;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private Set<Rating> ratings;

    public Recipe(String recipeName, List<String> steps, SocialMealsUser socialMealsUser) {
        this.recipeName = recipeName;
        this.steps = steps;
        this.socialMealsUser = socialMealsUser;
    }

    public Recipe() {
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public Long getUrlId() {
        return urlId;
    }

    public void setUrlId(Long urlId) {
        this.urlId = urlId;
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

    public byte[] getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(byte[] recipeImage) {
        this.recipeImage = recipeImage;
    }

    public Set<IngredientRecipe> getIngredientRecipes() {
        return ingredientRecipes;
    }

    public void setIngredientRecipes(Set<IngredientRecipe> ingredientRecipes) {
        this.ingredientRecipes = ingredientRecipes;
    }

    public SocialMealsUser getSocialMealsUser() {
        return socialMealsUser;
    }

    public void setSocialMealsUser(SocialMealsUser socialMealsUser) {
        this.socialMealsUser = socialMealsUser;
    }

    public Set<Cookbook> getCookbookLikes() {
        return cookbookLikes;
    }

    public void setCookbookLikes(Set<Cookbook> cookbookLikes) {
        this.cookbookLikes = cookbookLikes;
    }

    public Set<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(Set<Rating> ratings) {
        this.ratings = ratings;
    }
}
