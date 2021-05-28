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

    @ManyToMany(mappedBy = "likedRecipes")
    private Set<Ingredient> likes;

    public String getRecipeName() {
        return recipeName;
    }

    public Set<Ingredient> getLikes() {
        return likes;
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
