package nl.miwgroningen.cohort5.socialmeals.seeder;

import nl.miwgroningen.cohort5.socialmeals.dto.CookbookDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.IngredientRecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.SocialMealsUserDTO;
import nl.miwgroningen.cohort5.socialmeals.model.Ingredient;
import nl.miwgroningen.cohort5.socialmeals.model.IngredientRecipe;
import nl.miwgroningen.cohort5.socialmeals.model.Recipe;
import nl.miwgroningen.cohort5.socialmeals.repository.IngredientRecipeRepository;
import nl.miwgroningen.cohort5.socialmeals.service.CookbookService;
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
    private CookbookService cookbookService;

    @Autowired
    public Seeder(RecipeService recipeService,
                  IngredientService ingredientService,
                  SocialMealsUserDetailService socialMealsUserDetailService,
                  CookbookService cookbookService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.socialMealsUserDetailService = socialMealsUserDetailService;
        this.cookbookService = cookbookService;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedUser();
        seedIngredients();
        seedRecipes();
        seedIngredientRecipes();
        seedCookbooks();
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

        recipeService.addNew(new RecipeDTO("Lasagna", steps2, socialMealsUserDTO));
        recipeService.addNew(new RecipeDTO("Baba ganoush", steps, socialMealsUserDTO));
        recipeService.addNew(new RecipeDTO("Poké Bowl", new ArrayList<>(), socialMealsUserDTO));
        recipeService.addNew(new RecipeDTO("Strawberry smoothie", new ArrayList<>(), socialMealsUserDTO));
        recipeService.addNew(new RecipeDTO("Chickpea dahl", new ArrayList<>(), socialMealsUserDTO));
        recipeService.addNew(new RecipeDTO("Sweet potato curry", new ArrayList<>(), socialMealsUserDTO));

    }

    private void seedIngredients() {
        ingredientService.addNew(new Ingredient("Tomato"));
        ingredientService.addNew(new Ingredient("Eggplant"));
        ingredientService.addNew(new Ingredient("Garlic"));
        ingredientService.addNew(new Ingredient("Coriander"));
        ingredientService.addNew(new Ingredient("Rice"));
        ingredientService.addNew(new Ingredient("Broth"));
        ingredientService.addNew(new Ingredient("Pepper"));
        ingredientService.addNew(new Ingredient("Olive oil"));
        ingredientService.addNew(new Ingredient("Tahini"));
        ingredientService.addNew(new Ingredient("Lemon"));
        ingredientService.addNew(new Ingredient("Cumin"));
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

    private void seedCookbooks() {
        SocialMealsUserDTO socialMealsUserDTO = socialMealsUserDetailService.getUserByUsername("dummieChef");

        cookbookService.addNew(new CookbookDTO("Favorieten", socialMealsUserDTO, new ArrayList<>()));
        cookbookService.addNew(new CookbookDTO("Zomergerechten", socialMealsUserDTO, new ArrayList<>()));
        cookbookService.addNew(new CookbookDTO("Wintergerechten", socialMealsUserDTO, new ArrayList<>()));


        CookbookDTO cookbookDTO = new CookbookDTO("Libanees", socialMealsUserDTO, new ArrayList<>());
        cookbookService.addNew(cookbookDTO);
        RecipeDTO recipeDTO = recipeService.findByRecipeName("Lasagna");
        cookbookService.addRecipeDTO(cookbookDTO, recipeDTO);
    }



}
