package nl.miwgroningen.cohort5.socialmeals.controller;

import nl.miwgroningen.cohort5.socialmeals.dto.CookbookDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.SocialMealsUserDTO;
import nl.miwgroningen.cohort5.socialmeals.service.CookbookService;
import nl.miwgroningen.cohort5.socialmeals.service.RecipeService;
import nl.miwgroningen.cohort5.socialmeals.service.implementation.SocialMealsUserDetailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Britt van Mourik
 * <p>
 * Controls the view of MyKitchen and Cookbook
 */

@Controller
public class MyKitchenController {

    private final SocialMealsUserDetailService socialMealsUserDetailService;
    private final RecipeService recipeService;
    private final CookbookService cookbookService;

    public MyKitchenController(SocialMealsUserDetailService socialMealsUserDetailService,
                               RecipeService recipeService,
                               CookbookService cookbookService) {
        this.socialMealsUserDetailService = socialMealsUserDetailService;
        this.recipeService = recipeService;
        this.cookbookService = cookbookService;
    }

    @GetMapping("/MyKitchen")
    protected String showMyKitchen(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/";
        }

        SocialMealsUserDTO userDTO = socialMealsUserDetailService.getUserByUsername(principal.getName());
        List<CookbookDTO> cookbookDTOS = cookbookService.getCookbooksByUser(userDTO);

        model.addAttribute("cookbookList", cookbookDTOS);
        model.addAttribute("user", userDTO);
        return "myKitchen";
    }

    @GetMapping("/recipe/delete/{urlId}")
    protected String deleteRecipe(@PathVariable("urlId") Long urlId,
                                  Principal principal) {
        RecipeDTO recipeDTO = recipeService.findByUrlId(urlId);
        if (recipeUserDoesNotMatchCurrentUser(principal, recipeDTO)) {
            return "redirect:/MyKitchen";
        }

        recipeService.deleteRecipe(recipeDTO);
        return "redirect:/cookbook/myCookbook/" + principal.getName();
    }

    @GetMapping("/MyKitchen/addCookbook")
    protected String createCookbook(Model model) {
        model.addAttribute("cookbookDTO", new CookbookDTO());
        return "cookbookForm";
    }

    private boolean recipeUserDoesNotMatchCurrentUser(Principal principal, RecipeDTO recipeDTO) {
        SocialMealsUserDTO currentUser = socialMealsUserDetailService.getUserByUsername(principal.getName());
        return !currentUser.equals(recipeDTO.getSocialMealsUserDTO());
    }


}
