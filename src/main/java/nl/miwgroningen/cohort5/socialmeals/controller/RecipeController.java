package nl.miwgroningen.cohort5.socialmeals.controller;

import nl.miwgroningen.cohort5.socialmeals.repository.RecipeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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


}
