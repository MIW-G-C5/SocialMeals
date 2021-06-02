package nl.miwgroningen.cohort5.socialmeals.controller;

import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.service.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Wessel van Dommelen <w.r.van.dommelen@st.hanze.nl>
 */

@Controller
public class RecipeController {

    private RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipes")
    protected String showRecipes(Model model) {
        model.addAttribute("allRecipes", recipeService.getAll());
        return "recipeOverview";
    }

    @GetMapping("/recipes/{recipeName}")
    protected String showRecipeDetails(@PathVariable("recipeName") String recipeName, Model model) {
        RecipeDTO recipe = recipeService.findByRecipeName(recipeName);
        if (recipe == null) {
            return "redirect:/recipes";
        }
        model.addAttribute("recipe", recipe);
        model.addAttribute("ingredientRecipes", recipeService.getIngredientRecipesByRecipeName(recipeName));
        return "recipeDetails";
    }
}
