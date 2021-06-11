package nl.miwgroningen.cohort5.socialmeals.controller;

import nl.miwgroningen.cohort5.socialmeals.comparator.RecipeDTOAscComparator;
import nl.miwgroningen.cohort5.socialmeals.dto.IngredientRecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.SocialMealsUserDTO;
import nl.miwgroningen.cohort5.socialmeals.service.IngredientService;
import nl.miwgroningen.cohort5.socialmeals.service.RecipeService;
import nl.miwgroningen.cohort5.socialmeals.service.implementation.SocialMealsUserDetailService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

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
        List<RecipeDTO> recipeDTOS = recipeService.getAll();
        Collections.sort(recipeDTOS, new RecipeDTOAscComparator());

        model.addAttribute("allRecipes", recipeDTOS);
        model.addAttribute("comparator", new RecipeDTOAscComparator());
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
    protected String saveRecipe(@ModelAttribute("recipeDTO") RecipeDTO recipeDTO,
                                Model model,
                                BindingResult result,
                                Principal principal) {
        if (result.hasErrors()) {
            return "redirect:/";
        }

        SocialMealsUserDTO socialMealsUserDTO = socialMealsUserDetailService.getUserByUsername(principal.getName());
        if (socialMealsUserDTO != null) {
            recipeDTO.setSocialMealsUserDTO(socialMealsUserDTO);
        }

        try {
            recipeService.addNew(recipeDTO);
        } catch (DataIntegrityViolationException error) {
            return createRecipeFormWithNotificationRecipeExists(model, recipeDTO);
        }

        return "redirect:/recipes/update/" + stringURLify(recipeDTO.getRecipeName());
    }

    @PostMapping("/recipes/update/{recipeName}")
    protected String updateRecipe(@PathVariable("recipeName") String recipeName,
                                  @ModelAttribute("recipeDTO") RecipeDTO recipeDTO,
                                  Model model,
                                  BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/MyKitchen";
        }

        try {
            recipeService.updateRecipe(recipeService.findByRecipeName(recipeName), recipeDTO);
        } catch (DataIntegrityViolationException error) {
            return createRecipeUpdateFormWithNotificationRecipeExists(model, recipeDTO, recipeName);
        }

        return "redirect:/recipes/update/" + stringURLify(recipeName);
    }

    @PostMapping(value = "/recipes/update/{recipeName}/addingredient")
    protected String addIngredient(@ModelAttribute("ingredientRecipeDTO") IngredientRecipeDTO ingredientRecipeDTO,
                                   @PathVariable("recipeName") String recipeName,
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

    @GetMapping(value = "recipes/search")
    protected String searchRecipe(Model model, @RequestParam String keyword) {
        List<String> searchResults = recipeService.search(keyword);
        List<RecipeDTO> recipeResults = new ArrayList<>();
        for (String searchResult : searchResults) {
            recipeResults.add(recipeService.findByRecipeName(searchResult));
        }
        model.addAttribute("allRecipes", recipeResults);
        return "recipeOverview";
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

    private String createRecipeFormWithNotificationRecipeExists(Model model, RecipeDTO recipeDTO) {
        model.addAttribute("recipeDTO", new RecipeDTO());
        model.addAttribute("existingRecipe", recipeDTO);
        return "recipeForm";
    }

    private String createRecipeUpdateFormWithNotificationRecipeExists(Model model, RecipeDTO duplicateRecipeDTO, String recipeName) {
        model.addAttribute("existingRecipe", duplicateRecipeDTO);

        RecipeDTO recipeDTO = recipeService.findByRecipeName(recipeName);
        model.addAttribute("recipeDTO", recipeDTO);
        model.addAttribute("ingredientRecipeDTO", new IngredientRecipeDTO());
        model.addAttribute("presentIngredientsRecipes", recipeService.getIngredientRecipesByRecipeName(recipeName));
        model.addAttribute("remainingIngredients", recipeService.getRemainingIngredientsByRecipeName(recipeName));

        return "updateRecipeForm";
    }


}
