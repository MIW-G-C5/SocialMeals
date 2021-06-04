package nl.miwgroningen.cohort5.socialmeals.controller;

import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.SocialMealsUserDTO;
import nl.miwgroningen.cohort5.socialmeals.service.RecipeService;
import nl.miwgroningen.cohort5.socialmeals.service.implementation.SocialMealsUserDetailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

/**
 * Britt van Mourik
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

        SocialMealsUserDTO userDTO = socialMealsUserDetailService.getUserByUsername(principal.getName());

        if (userDTO == null) {
            return "redirect:/";
        }

        model.addAttribute("user", userDTO);
        model.addAttribute("userRecipes", recipeService.getRecipesByUsername(principal.getName()));
        return "myKitchen";
    }
}
