package nl.miwgroningen.cohort5.socialmeals.controller;


import nl.miwgroningen.cohort5.socialmeals.comparator.RecipeDTOAscComparator;
import nl.miwgroningen.cohort5.socialmeals.comparator.RecipeDTODescComparator;
import nl.miwgroningen.cohort5.socialmeals.dto.RatingDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.SocialMealsUserDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.CookbookDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.stateKeeper.SortedRecipesStateKeeper;
import nl.miwgroningen.cohort5.socialmeals.service.RatingService;
import nl.miwgroningen.cohort5.socialmeals.service.RecipeService;
import nl.miwgroningen.cohort5.socialmeals.service.CookbookService;
import nl.miwgroningen.cohort5.socialmeals.service.implementation.SocialMealsUserDetailService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

/**
 * @author Wessel van Dommelen <w.r.van.dommelen@st.hanze.nl>
 *
 * Controls the view of the recipes overview page
 */

@Controller
@SessionAttributes("sortedRecipesStateKeeper")
public class RecipeReadController {

    private final RecipeService recipeService;
    private final SocialMealsUserDetailService socialMealsUserDetailService;
    private final CookbookService cookbookService;
    private final RatingService ratingService;

    public RecipeReadController(RecipeService recipeService,
                                SocialMealsUserDetailService socialMealsUserDetailService,
                                CookbookService cookbookService,
                                RatingService ratingService) {
        this.recipeService = recipeService;
        this.socialMealsUserDetailService = socialMealsUserDetailService;
        this.cookbookService = cookbookService;
        this.ratingService = ratingService;
    }

    @ModelAttribute("sortedRecipesStateKeeper")
    public SortedRecipesStateKeeper sortedRecipeStateKeeper(){
        return new SortedRecipesStateKeeper();
    }

    @GetMapping({"/", "/recipes"})
    protected String showRecipes(
            @ModelAttribute("sortedRecipesStateKeeper") SortedRecipesStateKeeper sortedRecipesStateKeeper,
            Model model) {

        List<RecipeDTO> recipeDTOList = recipeService.getAll();
        sortedRecipesStateKeeper.setSortedRecipes(recipeDTOList);
        sortedRecipesStateKeeper.getSortedRecipes().sort(new RecipeDTOAscComparator());

        model.addAttribute("recipeList", sortedRecipesStateKeeper.getSortedRecipes());

        return "recipeOverview";
    }

    @GetMapping("/recipes/asc")
    protected String sortAscRecipes(
            @SessionAttribute("sortedRecipesStateKeeper") SortedRecipesStateKeeper sortedRecipesStateKeeper,
            Model model){

        sortedRecipesStateKeeper.getSortedRecipes().sort(new RecipeDTOAscComparator());

        model.addAttribute("recipeList", sortedRecipesStateKeeper.getSortedRecipes());

        return "recipeOverview";
    }

    @GetMapping("/recipes/desc")
    protected String sortDescRecipes(
            @SessionAttribute("sortedRecipesStateKeeper") SortedRecipesStateKeeper sortedRecipesStateKeeper,
            Model model){

        sortedRecipesStateKeeper.getSortedRecipes().sort(new RecipeDTODescComparator());

        model.addAttribute("recipeList", sortedRecipesStateKeeper.getSortedRecipes());

        return "recipeOverview";
    }

    @GetMapping("/recipes/{urlId}")
    protected String showRecipeDetails(@PathVariable("urlId") Long urlId,
                                       Model model,
                                       Principal principal) {

        RecipeDTO recipe = recipeService.findByUrlId(urlId);

        if (recipe == null) {
            return "redirect:/recipes";
        }

        boolean loggedInUser = principal != null;
        if (loggedInUser) {
            SocialMealsUserDTO socialMealsUserDTO = socialMealsUserDetailService.getUserByUsername(principal.getName());
            List<CookbookDTO> cookbookDTOList = cookbookService.getCookbooksByUser(socialMealsUserDTO);
            model.addAttribute("cookbookList", cookbookDTOList);
        }

        model.addAttribute("recipeImage", Base64.encodeBase64String(recipe.getRecipeImage()));
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("recipe", recipe);
        model.addAttribute("ingredientRecipes", recipeService.getIngredientRecipesByRecipeUrlId(urlId));

        model.addAttribute("rating", new RatingDTO());

        return "recipeDetails";
    }

    @GetMapping(value = "/recipes/search")
    protected String searchRecipe(@SessionAttribute("sortedRecipesStateKeeper") SortedRecipesStateKeeper sortedRecipesStateKeeper,
                                  Model model, @RequestParam String keyword) {
        List<Long> searchResults = recipeService.search(keyword);
        List<RecipeDTO> sortedRecipes = new ArrayList<>();

        for (Long searchResult : searchResults) {
            sortedRecipes.add(recipeService.findByUrlId(searchResult));
        }

        sortedRecipesStateKeeper.setSortedRecipes(sortedRecipes);
        sortedRecipesStateKeeper.getSortedRecipes().sort(new RecipeDTOAscComparator());
        model.addAttribute("recipeList", sortedRecipesStateKeeper.getSortedRecipes());

        return "recipeOverview";
    }

    @PostMapping("/recipes/{recipeUrlId}/rate")
    protected String saveRating(@PathVariable("recipeUrlId") Long urlId,
                                @RequestParam("rating") int rating,
                                RatingDTO ratingDTO,
                                Principal principal,
                                BindingResult result) {
        SocialMealsUserDTO socialMealsUserDTO = socialMealsUserDetailService.getUserByUsername(principal.getName());
        RecipeDTO recipeDTO = recipeService.findByUrlId(urlId);

        if (!result.hasErrors()) {
            ratingService.addNew(new RatingDTO(rating, recipeDTO, socialMealsUserDTO));
        }

        return "redirect:/recipes/" + urlId;
    }
}
