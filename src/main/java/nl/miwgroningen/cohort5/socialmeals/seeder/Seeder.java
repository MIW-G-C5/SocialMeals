package nl.miwgroningen.cohort5.socialmeals.seeder;

import nl.miwgroningen.cohort5.socialmeals.dto.IngredientRecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.model.Ingredient;
import nl.miwgroningen.cohort5.socialmeals.model.IngredientRecipe;
import nl.miwgroningen.cohort5.socialmeals.model.Recipe;
import nl.miwgroningen.cohort5.socialmeals.repository.IngredientRecipeRepository;
import nl.miwgroningen.cohort5.socialmeals.service.IngredientService;
import nl.miwgroningen.cohort5.socialmeals.service.RecipeService;
import nl.miwgroningen.cohort5.socialmeals.service.implementation.SocialMealsUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author A.H. van Zessen
 */

@Component
public class Seeder {

    private RecipeService recipeService;
    private IngredientService ingredientService;
    private SocialMealsUserDetailService socialMealsUserDetailService;

    @Autowired
    public Seeder(RecipeService recipeService,
                  IngredientService ingredientService,
                  SocialMealsUserDetailService socialMealsUserDetailService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.socialMealsUserDetailService = socialMealsUserDetailService;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedUser();
        seedIngredients();
        seedRecipes();
        seedIngredientRecipes();
    }

    private void seedUser() {
        if (socialMealsUserDetailService.getAll().size() == 0) {
            socialMealsUserDetailService.addSocialMealsUser("admin", "admin");
        }
    }

    private void seedRecipes() {
        List<String> steps = new ArrayList<>(List.of("Vies", "jakkie", "bah"));
        List<String> steps2 = new ArrayList<>(List.of("Knoflook bakken", "ui erbij", "paprika"));

        recipeService.addNew(new Recipe("Poep", steps));
        recipeService.addNew(new Recipe("Lasagna", steps2));
    }

    private void seedIngredients() {
        ingredientService.addNew(new Ingredient("tomaat"));
        ingredientService.addNew(new Ingredient("aubergine"));
        ingredientService.addNew(new Ingredient("feces"));
    }

    private void seedIngredientRecipes() {
        List<IngredientRecipeDTO> ingredientRecipeList = new ArrayList<>();
        ingredientRecipeList.add(
                new IngredientRecipeDTO(ingredientService.findByIngredientName("tomaat"),
                recipeService.findByRecipeName("Lasagna"),
                5,
                "stuks"));

        recipeService.addIngredientsToRecipe(ingredientRecipeList);

    }

}
