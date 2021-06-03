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

    private String recipeName;

    @ElementCollection
    private List<String> steps = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<IngredientRecipe> ingredientRecipes;

    public Recipe(String recipeName, List<String> steps) {
        this.recipeName = recipeName;
        this.steps = steps;
    }

    public Recipe() {
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public Long getRecipeId() {
        return recipeId;
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

    public Set<IngredientRecipe> getIngredientRecipes() {
        return ingredientRecipes;
    }

    public void setIngredientRecipes(Set<IngredientRecipe> ingredientQuantities) {
        this.ingredientRecipes = ingredientQuantities;
    }
}
