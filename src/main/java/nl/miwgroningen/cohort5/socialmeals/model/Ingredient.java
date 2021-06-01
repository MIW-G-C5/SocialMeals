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

    @OneToMany(mappedBy = "ingredient")
    private Set<IngredientRecipe> ingredientQuantities;

    public Ingredient(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public Ingredient() {
    }

    public String getIngredientName() {
        return ingredientName;
    }
}
