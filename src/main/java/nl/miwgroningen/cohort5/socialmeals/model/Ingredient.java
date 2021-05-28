package nl.miwgroningen.cohort5.socialmeals.model;

import javax.persistence.*;
import java.util.Set;

/**
 * @author A.H. van Zessen
 *
 * Describes the details of an ingredient
 */

@Entity
public class Ingredient {

    @Id
    @GeneratedValue
    private Long ingredientId;

    private String ingredientName;

    @ManyToMany
    @JoinTable(name = "recipe_like",
            joinColumns = @JoinColumn(name = "ingredient_Id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_Id"))
    private Set<Recipe> likedRecipes;

    public String getIngredientName() {
        return ingredientName;
    }
}
