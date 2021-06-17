package nl.miwgroningen.cohort5.socialmeals.controller;

import nl.miwgroningen.cohort5.socialmeals.comparator.RecipeDTOAscComparator;
import nl.miwgroningen.cohort5.socialmeals.dto.CookbookDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.SocialMealsUserDTO;
import nl.miwgroningen.cohort5.socialmeals.service.CookbookService;
import nl.miwgroningen.cohort5.socialmeals.service.RecipeService;
import nl.miwgroningen.cohort5.socialmeals.service.implementation.SocialMealsUserDetailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

/**
 * Britt van Mourik
 * Controls the view of Cookbooks
 */

@Controller
public class CookbookController {

    private CookbookService cookbookService;
    private SocialMealsUserDetailService socialMealsUserDetailService;
    private RecipeService recipeService;

    public CookbookController(CookbookService cookbookService,
                              SocialMealsUserDetailService socialMealsUserDetailService, RecipeService recipeService) {
        this.cookbookService = cookbookService;
        this.socialMealsUserDetailService = socialMealsUserDetailService;
        this.recipeService = recipeService;
    }

    @GetMapping("/cookbook/{urlId}")
    protected String showPublicCookbook(@PathVariable("urlId") Long urlId,
                                        Model model,
                                        Principal principal){
        CookbookDTO cookbookDTO = cookbookService.findByUrlId(urlId);
        SocialMealsUserDTO socialMealsUserDTO =
                socialMealsUserDetailService.getUserByUsername(cookbookDTO.getSocialMealsUser().getUsername());

        model.addAttribute("cookbookDTO", cookbookDTO);
        model.addAttribute("ownRecipe", isItYours(principal, socialMealsUserDTO));
        model.addAttribute("socialMealsUserDTO", socialMealsUserDTO);
        model.addAttribute("recipeDTOList", cookbookDTO.getRecipes());

        return "cookbookDetails";
    }

    @GetMapping("/cookbook/myCookbook/{username}")
    protected String showMyCookbook(@PathVariable("username") String username,
                                    Model model,
                                    Principal principal) {
        List<RecipeDTO> recipeDTOList = recipeService.getRecipesByUsername(username);
        Collections.sort(recipeDTOList, new RecipeDTOAscComparator());
        SocialMealsUserDTO socialMealsUserDTO = socialMealsUserDetailService.getUserByUsername(username);

        if (recipeDTOList == null) {
            return "redirect:/";
        }

        model.addAttribute("ownRecipe", isItYours(principal, socialMealsUserDTO));
        model.addAttribute("socialMealsUserDTO", socialMealsUserDTO);
        model.addAttribute("recipeDTOList", recipeDTOList);

        return "myCookbook";
    }

    @PostMapping("/cookbook/new")
    protected String saveNewCookbook(@ModelAttribute("cookbookDTO") CookbookDTO cookbookDTO,
                                     BindingResult result,
                                     Principal principal) {
        if (result.hasErrors()) {
            return "redirect:/MyKitchen";
        }

        SocialMealsUserDTO socialMealsUserDTO = socialMealsUserDetailService.getUserByUsername(principal.getName());
        if (socialMealsUserDTO != null) {
            cookbookDTO.setSocialMealsUser(socialMealsUserDTO);
        }

        cookbookService.addNew(cookbookDTO);

        return "redirect:/MyKitchen";
    }

    @GetMapping("/cookbook/add/{urlId}/{recipeName}")
    protected String addRecipeToCookbook(@PathVariable("urlId") Long urlId,
                                         @PathVariable("recipeName") String recipeName,
                                         Principal principal) {

        CookbookDTO cookbookDTO = cookbookService.findByUrlId(urlId);
        if (isItYours(principal, cookbookDTO.getSocialMealsUser())) {
            cookbookService.addRecipeDTO(cookbookDTO, recipeService.findByRecipeName(recipeName));
        }

        return "redirect:/recipes/" + recipeName;
    }

     @GetMapping("/cookbook/remove/{urlId}/{recipeName}")
     protected String removeRecipeFromCookbook(@PathVariable("urlId") Long urlId,
                                               @PathVariable("recipeName") String recipeName,
                                               Principal principal) {
         CookbookDTO cookbookDTO = cookbookService.findByUrlId(urlId);
         if (isItYours(principal, cookbookDTO.getSocialMealsUser())) {
             cookbookService.removeRecipeDTO(cookbookDTO, recipeService.findByRecipeName(recipeName));
         }

         return "redirect:/cookbook/" + cookbookDTO.getUrlId();
     }

    private boolean isItYours(Principal principal, SocialMealsUserDTO socialMealsUserDTO) {
        return principal.getName().equals(socialMealsUserDTO.getUsername());
    }



}