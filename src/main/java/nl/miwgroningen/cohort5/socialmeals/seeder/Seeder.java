package nl.miwgroningen.cohort5.socialmeals.seeder;

import nl.miwgroningen.cohort5.socialmeals.dto.*;
import nl.miwgroningen.cohort5.socialmeals.service.CookbookService;
import nl.miwgroningen.cohort5.socialmeals.service.IngredientService;
import nl.miwgroningen.cohort5.socialmeals.service.RatingService;
import nl.miwgroningen.cohort5.socialmeals.service.RecipeService;
import nl.miwgroningen.cohort5.socialmeals.service.implementation.SocialMealsUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author A.H. van Zessen
 */

@Component
public class Seeder {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final SocialMealsUserDetailService socialMealsUserDetailService;
    private final CookbookService cookbookService;
    private final RatingService ratingService;

    @Autowired
    public Seeder(RecipeService recipeService,
                  IngredientService ingredientService,
                  SocialMealsUserDetailService socialMealsUserDetailService,
                  CookbookService cookbookService,
                  RatingService ratingService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.socialMealsUserDetailService = socialMealsUserDetailService;
        this.cookbookService = cookbookService;
        this.ratingService = ratingService;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedUser();
        seedIngredients();
        seedRecipes();
        seedRecipeImages();
        seedIngredientRecipes();
        seedCookbooks();
        seedRatings();
    }

    private void seedUser() {
        if (socialMealsUserDetailService.getAll().size() == 0) {
            socialMealsUserDetailService.addSocialMealsUser("admin", "admin");
            socialMealsUserDetailService.addSocialMealsUser("Joop", "123");
            socialMealsUserDetailService.addSocialMealsUser("Harry", "123");
            socialMealsUserDetailService.addSocialMealsUser("Sara", "123");
            socialMealsUserDetailService.addSocialMealsUser("Douwe", "123");
            socialMealsUserDetailService.addSocialMealsUser("Arlette", "123");
            socialMealsUserDetailService.addSocialMealsUser("Daphne", "123");
            socialMealsUserDetailService.addSocialMealsUser("Victor", "123");
        }
    }

    private void seedRecipes() {

        List<String> steps = new ArrayList<>(babaGanoushSteps());
        List<String> steps2 = new ArrayList<>(List.of("Fry garlic", "and onion", "add everything and put in oven"));

        SocialMealsUserDTO socialMealsUserDTO = socialMealsUserDetailService.getUserByUsername("Joop");

        recipeService.addNew(new RecipeDTO("Lasagna", steps2, socialMealsUserDTO));
        recipeService.addNew(new RecipeDTO("Baba ganoush", steps, socialMealsUserDTO));
        recipeService.addNew(new RecipeDTO("Poké Bowl", new ArrayList<>(), socialMealsUserDTO));
        recipeService.addNew(new RecipeDTO("Strawberry smoothie", new ArrayList<>(), socialMealsUserDTO));
        recipeService.addNew(new RecipeDTO("Chickpea dahl", new ArrayList<>(), socialMealsUserDTO));
        recipeService.addNew(new RecipeDTO("Sweet potato curry", new ArrayList<>(), socialMealsUserDTO));
    }

    private void seedRecipeImages() {

        File babaGanoushImage = new File("src/main/resources/images/baba.ganoush.jpg");

        RecipeDTO recipeDTO = recipeService.findByUrlId(Long.valueOf(5002));
        recipeDTO.setRecipeImage(imageToByteArray(babaGanoushImage));

        recipeService.updateRecipeWithImage(recipeService.findByUrlId(Long.valueOf(5002)), recipeDTO);
    }

    private List<String> babaGanoushSteps() {
        List<String> steps = new ArrayList<>();

        steps.add("Preheat the oven to 230 degrees Celcius. Line a baking sheet with parchment paper. " +
                "Halve the eggplants lengthwise, brush them lightly with olive oil and roast for 30 to 40 minutes.");
        steps.add("Set the eggplants aside for a few minutes. Flip them over and scoop out the flesh with a spoon. " +
                "Discard the skin.");
        steps.add("Strain the eggplant flesh over a mixing bowl. " +
                "Let the eggplant rest for a few minutes and try again to remove more moisture. " +
                "Discard the moisture afterwards. ");
        steps.add("Clean and dry the mixing bowl and add the eggplant to it. " +
                "Add the garlic and lemon juice and stir with a fork until the eggplant breaks down.");
        steps.add("Add tahini and mix well. Slowly add olive oil while stirring. " +
                "Continue stirring until the mixture is pale and creamy.");
        steps.add("Add parsley, salt and cumin to the mixture. " +
                "Season with more salt and lemon juice if you like.");
        steps.add("Serve the baba ganoush with a light drizzle of olive oil " +
                "and some sprinkled parsley and smoked paprika on top.");

        return steps;
    }

    private void seedIngredients() {
        ingredientService.addNew(new IngredientDTO("Tomato"));
        ingredientService.addNew(new IngredientDTO("Eggplant"));
        ingredientService.addNew(new IngredientDTO("Garlic"));
        ingredientService.addNew(new IngredientDTO("Coriander"));
        ingredientService.addNew(new IngredientDTO("Rice"));
        ingredientService.addNew(new IngredientDTO("Broth"));
        ingredientService.addNew(new IngredientDTO("Pepper"));
        ingredientService.addNew(new IngredientDTO("Olive oil"));
        ingredientService.addNew(new IngredientDTO("Tahini"));
        ingredientService.addNew(new IngredientDTO("Lemon"));
        ingredientService.addNew(new IngredientDTO("Cumin"));
        ingredientService.addNew(new IngredientDTO("Parsley"));
        ingredientService.addNew(new IngredientDTO("Smoked paprika"));
    }

    private void seedIngredientRecipes() {
        List<IngredientRecipeDTO> ingredientRecipeList = new ArrayList<>();
        ingredientRecipeList.add(
                new IngredientRecipeDTO(ingredientService.findByIngredientName("tomato"),
                        recipeService.findByUrlId(Long.valueOf(5001)),
                        5,
                        "units"));
        ingredientRecipeList.add(
                new IngredientRecipeDTO(ingredientService.findByIngredientName("eggplant"),
                        recipeService.findByUrlId(Long.valueOf(5002)),
                        6,
                        "units"));
        ingredientRecipeList.add(
                new IngredientRecipeDTO(ingredientService.findByIngredientName("Olive oil"),
                        recipeService.findByUrlId(Long.valueOf(5002)),
                        3,
                        "tablespoons"));
        ingredientRecipeList.add(
                new IngredientRecipeDTO(ingredientService.findByIngredientName("Parsley"),
                        recipeService.findByUrlId(Long.valueOf(5002)),
                        2,
                        "teaspoons"));
        ingredientRecipeList.add(
                new IngredientRecipeDTO(ingredientService.findByIngredientName("Smoked paprika"),
                        recipeService.findByUrlId(Long.valueOf(5002)),
                        1,
                        "teaspoon"));
        ingredientRecipeList.add(
                new IngredientRecipeDTO(ingredientService.findByIngredientName("tahini"),
                        recipeService.findByUrlId(Long.valueOf(5002)),
                        2,
                        "tablespoons"));
        ingredientRecipeList.add(
                new IngredientRecipeDTO(ingredientService.findByIngredientName("cumin"),
                        recipeService.findByUrlId(Long.valueOf(5002)),
                        10,
                        "grams"));
        ingredientRecipeList.add(
                new IngredientRecipeDTO(ingredientService.findByIngredientName("lemon"),
                        recipeService.findByUrlId(Long.valueOf(5002)),
                        5,
                        "squeezes"));

        recipeService.addIngredientsToRecipe(ingredientRecipeList);

    }

    private void seedCookbooks() {
        SocialMealsUserDTO socialMealsUserDTO = socialMealsUserDetailService.getUserByUsername("Joop");

        cookbookService.addNew(new CookbookDTO("Favorieten", socialMealsUserDTO, new ArrayList<>()));
        cookbookService.addNew(new CookbookDTO("Zomergerechten", socialMealsUserDTO, new ArrayList<>()));
        cookbookService.addNew(new CookbookDTO("Wintergerechten", socialMealsUserDTO, new ArrayList<>()));


        CookbookDTO cookbookDTO = new CookbookDTO("Libanees", socialMealsUserDTO, new ArrayList<>());
        cookbookService.addNew(cookbookDTO);
        RecipeDTO recipeDTO = recipeService.findByUrlId(Long.valueOf(5001));
        cookbookService.addRecipeDTO(cookbookDTO, recipeDTO);
    }

    private void seedRatings() {
        SocialMealsUserDTO joop = socialMealsUserDetailService.getUserByUsername("Joop");
        SocialMealsUserDTO harry = socialMealsUserDetailService.getUserByUsername("Harry");
        SocialMealsUserDTO sara = socialMealsUserDetailService.getUserByUsername("Sara");
        SocialMealsUserDTO douwe = socialMealsUserDetailService.getUserByUsername("Douwe");
        SocialMealsUserDTO arlette = socialMealsUserDetailService.getUserByUsername("Arlette");
        SocialMealsUserDTO daphne = socialMealsUserDetailService.getUserByUsername("Daphne");
        SocialMealsUserDTO victor = socialMealsUserDetailService.getUserByUsername("Victor");

        RecipeDTO recipeDTO = recipeService.findByUrlId(Long.valueOf(5002));
        RecipeDTO recipeDTO2 = recipeService.findByUrlId(Long.valueOf(5003));
        RecipeDTO recipeDTO3 = recipeService.findByUrlId(Long.valueOf(5004));

        ratingService.addNew(new RatingDTO(4, recipeDTO, joop));
        ratingService.addNew(new RatingDTO(3, recipeDTO, harry));
        ratingService.addNew(new RatingDTO(2, recipeDTO, sara));
        ratingService.addNew(new RatingDTO(5, recipeDTO, douwe));
        ratingService.addNew(new RatingDTO(5, recipeDTO, arlette));
        ratingService.addNew(new RatingDTO(4, recipeDTO, daphne));
        ratingService.addNew(new RatingDTO(5, recipeDTO, victor));

        ratingService.addNew(new RatingDTO(3, recipeDTO2, joop));
        ratingService.addNew(new RatingDTO(2, recipeDTO2, harry));
        ratingService.addNew(new RatingDTO(1, recipeDTO2, sara));
        ratingService.addNew(new RatingDTO(4, recipeDTO2, douwe));
        ratingService.addNew(new RatingDTO(3, recipeDTO2, daphne));


        ratingService.addNew(new RatingDTO(4, recipeDTO3, joop));
        ratingService.addNew(new RatingDTO(3, recipeDTO3, harry));
        ratingService.addNew(new RatingDTO(5, recipeDTO3, sara));
    }

    private byte[] imageToByteArray(File image) {

        byte[] data = null;

        try {
            BufferedImage bImage = ImageIO.read(image);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bImage, "jpg", outputStream);
            data = outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}
