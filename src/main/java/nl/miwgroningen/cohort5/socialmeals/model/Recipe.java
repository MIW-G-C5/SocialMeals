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

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }
}
