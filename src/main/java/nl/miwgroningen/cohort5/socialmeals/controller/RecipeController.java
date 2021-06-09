package nl.miwgroningen.cohort5.socialmeals.controller;

import nl.miwgroningen.cohort5.socialmeals.dto.IngredientRecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.SocialMealsUserDTO;
import nl.miwgroningen.cohort5.socialmeals.service.IngredientService;
import nl.miwgroningen.cohort5.socialmeals.service.RecipeService;
import nl.miwgroningen.cohort5.socialmeals.service.implementation.SocialMealsUserDetailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author Wessel van Dommelen <w.r.van.dommelen@st.hanze.nl>
 *
 * Controls the view of allrecipes page and creating and updating pages
 */

@Controller
public class RecipeController {

    private RecipeService recipeService;
    private IngredientService ingredientService;
    private SocialMealsUserDetailService socialMealsUserDetailService;

    public RecipeController(RecipeService recipeService, IngredientService ingredientService,
                            SocialMealsUserDetailService socialMealsUserDetailService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.socialMealsUserDetailService = socialMealsUserDetailService;
    }

    @GetMapping({"/", "/recipes"})
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

    @GetMapping("/recipes/new")
    protected String showRecipeForm(Model model) {
        model.addAttribute("recipeDTO", new RecipeDTO());
        return "recipeForm";
    }

    @PostMapping("/recipes/new")
    protected String saveRecipe(@ModelAttribute("recipeDTO") RecipeDTO recipeDTO, BindingResult result, Principal principal) {
        if (result.hasErrors()) {
            return "redirect:/";
        }

        SocialMealsUserDTO socialMealsUserDTO = socialMealsUserDetailService.getUserByUsername(principal.getName());
        if (socialMealsUserDTO != null) {
            recipeDTO.setSocialMealsUserDTO(socialMealsUserDTO);
        }

        try {
            recipeService.addNew(recipeDTO);
        } catch (Exception error) {
            System.err.println("Recipe already exists");
            return "redirect:/MyKitchen";
        }

        return "redirect:/recipes/update/" + stringURLify(recipeDTO.getRecipeName());

    }

    @GetMapping("/recipes/update/{recipeName}")
    protected String showUpdateRecipe(@PathVariable("recipeName") String recipeName, Model model) {
        RecipeDTO recipe = recipeService.findByRecipeName(recipeName);
        if (recipe == null) {
            return "redirect:/";
        }

        model.addAttribute("ingredientRecipeDTO", new IngredientRecipeDTO());
        model.addAttribute("presentIngredientsRecipes", recipeService.getIngredientRecipesByRecipeName(recipeName));
        model.addAttribute("remainingIngredients", recipeService.getRemainingIngredientsByRecipeName(recipeName));
        model.addAttribute("recipeDTO", recipe);
        return "updateRecipeForm";
    }

    @PostMapping("/recipes/update/{recipeName}")
    protected String updateRecipe(@PathVariable("recipeName") String recipeName,
                                  @ModelAttribute("recipeDTO") RecipeDTO recipeDTO,
                                  BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/";
        }

        try {
            recipeService.updateRecipe(recipeService.findByRecipeName(recipeName), recipeDTO);
        } catch (Exception error) {
            System.err.println("Recipe already exists");
        }

        return "redirect:/recipes/update/" + stringURLify(recipeName);
    }

    @PostMapping("/recipes/{recipeName}/addingredient")
    protected String addIngredient(@PathVariable("recipeName") String recipeName,
                                   @ModelAttribute("ingredientRecipeDTO") IngredientRecipeDTO ingredientRecipeDTO,
                                   @RequestParam("ingredientName") String ingredientName,
                                   BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/";
        }

        try {
            ingredientRecipeDTO.setRecipeDTO(recipeService.findByRecipeName(recipeName));
            ingredientRecipeDTO.setIngredientDTO(ingredientService.findByIngredientName(ingredientName));
            recipeService.addIngredientToRecipe(ingredientRecipeDTO);
        } catch (Exception error) {
            System.err.println(error.getMessage());
        }

        return "redirect:/recipes/update/" + stringURLify(recipeName);
    }

    public String stringURLify(String name) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (c == ' ') {
                stringBuilder.append("%20");
            } else {
                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();
    }
}
