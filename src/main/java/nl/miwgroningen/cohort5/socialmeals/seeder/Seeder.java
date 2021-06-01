package nl.miwgroningen.cohort5.socialmeals.seeder;

import nl.miwgroningen.cohort5.socialmeals.model.Ingredient;
import nl.miwgroningen.cohort5.socialmeals.model.Recipe;
import nl.miwgroningen.cohort5.socialmeals.service.IngredientService;
import nl.miwgroningen.cohort5.socialmeals.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;

/**
 * @author A.H. van Zessen
 */

@Component
public class Seeder {
    private RecipeService recipeService;
    private IngredientService ingredientService;

    @Autowired
    public Seeder(RecipeService recipeService, IngredientService ingredientService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedRecipes();
        seedIngredients();
    }

    private void seedRecipes() {
        recipeService.addNew(new Recipe("Lasagna", "Lekker"));
        recipeService.addNew(new Recipe("Poep", "Vies"));
    }

    private void seedIngredients() {
        ingredientService.addNew(new Ingredient("tomaat"));
        ingredientService.addNew(new Ingredient("aubergine"));
        ingredientService.addNew(new Ingredient("feces"));
    }

}
