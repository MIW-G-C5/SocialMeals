package nl.miwgroningen.cohort5.socialmeals.controller;

import nl.miwgroningen.cohort5.socialmeals.dto.IngredientRecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.SocialMealsUserDTO;
import nl.miwgroningen.cohort5.socialmeals.service.RecipeService;
import nl.miwgroningen.cohort5.socialmeals.service.implementation.SocialMealsUserDetailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Britt van Mourik
 *
 * Controls the view of MyKitchen
 */

@Controller
public class MyKitchenController {

    private SocialMealsUserDetailService socialMealsUserDetailService;
    private RecipeService recipeService;

    public MyKitchenController(SocialMealsUserDetailService socialMealsUserDetailService,
                                     RecipeService recipeService) {
        this.socialMealsUserDetailService = socialMealsUserDetailService;
        this.recipeService = recipeService;
    }

    @GetMapping("/MyKitchen")
    protected String showUserRecipes(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/";
        }

        SocialMealsUserDTO userDTO = socialMealsUserDetailService.getUserByUsername(principal.getName());

        model.addAttribute("user", userDTO);
        model.addAttribute("userRecipes", recipeService.getRecipesByUsername(principal.getName()));
        return "myKitchen";
    }

    @GetMapping("/recipes/delete/{recipeName}")
    protected String deleteRecipe(@PathVariable("recipeName") String recipeName,
                                  Principal principal) {
        RecipeDTO recipeDTO = recipeService.findByRecipeName(recipeName);
        if (recipeUserDoesNotMatchCurrentUser(principal, recipeDTO)) {
            return "redirect:/MyKitchen";
        }

        recipeService.deleteRecipe(recipeDTO);
        return "redirect:/MyKitchen";
    }

    @GetMapping("/recipes/update/{recipeName}")
    protected String showUpdateRecipe(@PathVariable("recipeName") String recipeName, Model model, Principal principal) {
        RecipeDTO recipeDTO = recipeService.findByRecipeName(recipeName);
        if (recipeDTO == null || recipeUserDoesNotMatchCurrentUser(principal, recipeDTO)) {
            return "redirect:/MyKitchen";
        }

        model.addAttribute("recipeDTO", recipeDTO);
        model.addAttribute("ingredientRecipeDTO", new IngredientRecipeDTO());
        model.addAttribute("presentIngredientsRecipes", recipeService.getIngredientRecipesByRecipeName(recipeName));
        model.addAttribute("remainingIngredients", recipeService.getRemainingIngredientsByRecipeName(recipeName));

        return "updateRecipeForm";
    }

    private boolean recipeUserDoesNotMatchCurrentUser(Principal principal, RecipeDTO recipeDTO) {
        SocialMealsUserDTO currentUser = socialMealsUserDetailService.getUserByUsername(principal.getName());
        return !currentUser.equals(recipeDTO.getSocialMealsUserDTO());
    }
}
