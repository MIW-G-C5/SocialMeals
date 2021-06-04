package nl.miwgroningen.cohort5.socialmeals.seeder;

import nl.miwgroningen.cohort5.socialmeals.dto.IngredientRecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.SocialMealsUserDTO;
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
import java.security.Principal;
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
            socialMealsUserDetailService.addSocialMealsUser("dummieChef", "123");
        }
    }

    private void seedRecipes() {
        List<String> steps = new ArrayList<>(List.of("Roast eggplant", "add all ingredients", "blend"));
        List<String> steps2 = new ArrayList<>(List.of("Fry garlic", "and onion", "add everything and put in oven"));

        SocialMealsUserDTO socialMealsUserDTO = socialMealsUserDetailService.getUserByUsername("dummieChef");

        recipeService.addNew(new RecipeDTO("Baba ganoush", steps, socialMealsUserDTO));
        recipeService.addNew(new RecipeDTO("Lasagna", steps2, socialMealsUserDTO));
    }

    private void seedIngredients() {
        ingredientService.addNew(new Ingredient("tomato"));
        ingredientService.addNew(new Ingredient("eggplant"));
        ingredientService.addNew(new Ingredient("garlic"));
        ingredientService.addNew(new Ingredient("tahini"));
        ingredientService.addNew(new Ingredient("lemon"));
        ingredientService.addNew(new Ingredient("cumin"));
    }

    private void seedIngredientRecipes() {
        List<IngredientRecipeDTO> ingredientRecipeList = new ArrayList<>();
        ingredientRecipeList.add(
                new IngredientRecipeDTO(ingredientService.findByIngredientName("tomato"),
                recipeService.findByRecipeName("Lasagna"),
                        5,
                        "units"));
        ingredientRecipeList.add(
                new IngredientRecipeDTO(ingredientService.findByIngredientName("eggplant"),
                recipeService.findByRecipeName("Baba ganoush"),
                        6,
                        "units"));
        ingredientRecipeList.add(
                new IngredientRecipeDTO(ingredientService.findByIngredientName("tahini"),
                        recipeService.findByRecipeName("Baba ganoush"),
                        2,
                        "tablespoons"));
        ingredientRecipeList.add(
                new IngredientRecipeDTO(ingredientService.findByIngredientName("cumin"),
                        recipeService.findByRecipeName("Baba ganoush"),
                        10,
                        "grams"));
        ingredientRecipeList.add(
                new IngredientRecipeDTO(ingredientService.findByIngredientName("lemon"),
                        recipeService.findByRecipeName("Baba ganoush"),
                        5,
                        "squeezes"));

        recipeService.addIngredientsToRecipe(ingredientRecipeList);

    }

}
