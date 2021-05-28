package nl.miwgroningen.cohort5.socialmeals.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
