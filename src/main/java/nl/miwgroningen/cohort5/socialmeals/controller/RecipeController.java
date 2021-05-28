package nl.miwgroningen.cohort5.socialmeals.controller;

import nl.miwgroningen.cohort5.socialmeals.model.Recipe;
import nl.miwgroningen.cohort5.socialmeals.repository.RecipeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

/**
 * @author Wessel van Dommelen <w.r.van.dommelen@st.hanze.nl>
 */

@Controller
public class RecipeController {

    private RecipeRepository recipeRepository;

    public RecipeController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @GetMapping("/recipes")
    protected String showRecipes(Model model) {
        model.addAttribute("allRecipes", recipeRepository.findAll());
        return "recipeOverview";
    }

    @GetMapping("/recipes/{recipeName}")
    protected String showRecipeDetails(@PathVariable("recipeName") String recipeName, Model model) {
        Optional<Recipe> recipes = recipeRepository.findByRecipeName(recipeName);
        if (recipes.isEmpty()) {
            return "redirect:/recipes";
        }

        model.addAttribute("recipe", recipes.get());
        return "recipeDetails";
    }


}
