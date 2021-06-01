package nl.miwgroningen.cohort5.socialmeals.model;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Wessel van Dommelen <w.r.van.dommelen@st.hanze.nl>
 *
 *     describes the details of a Recipe
 */
@Entity
public class Recipe {

    @Id
    @GeneratedValue
    private Long recipeId;

    private String recipeName;
    private String description;

    @OneToMany(mappedBy = "recipe")
    private Set<IngredientRecipe> ingredientQuantities;

    public Recipe(String recipeName, String description) {
        this.recipeName = recipeName;
        this.description = description;
    }

    public Recipe() {
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

    public Set<IngredientRecipe> getIngredientQuantities() {
        return ingredientQuantities;
    }
}
