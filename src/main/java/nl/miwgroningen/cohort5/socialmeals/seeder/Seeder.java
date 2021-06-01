package nl.miwgroningen.cohort5.socialmeals.seeder;

import nl.miwgroningen.cohort5.socialmeals.model.Recipe;
import nl.miwgroningen.cohort5.socialmeals.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author A.H. van Zessen
 */

@Component
public class Seeder {
    private RecipeService recipeService;

    @Autowired
    public Seeder(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedRecipes();
    }

    private void seedRecipes() {
        recipeService.addNew(new Recipe("Lasagna", "Lekker"));
        recipeService.addNew(new Recipe("Poep", "Vies"));
    }
}
