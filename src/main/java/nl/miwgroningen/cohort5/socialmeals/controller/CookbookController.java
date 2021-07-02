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
import java.util.List;

/**
 * Britt van Mourik
 * Controls the view of Cookbooks
 */

@Controller
public class CookbookController {

    private final CookbookService cookbookService;
    private final SocialMealsUserDetailService socialMealsUserDetailService;
    private final RecipeService recipeService;

    public CookbookController(CookbookService cookbookService,
                              SocialMealsUserDetailService socialMealsUserDetailService, RecipeService recipeService) {
        this.cookbookService = cookbookService;
        this.socialMealsUserDetailService = socialMealsUserDetailService;
        this.recipeService = recipeService;
    }

    @GetMapping("/cookbook/{urlId}")
    protected String showPublicCookbook(@PathVariable("urlId") Long urlId,
                                        Model model,
                                        Principal principal) {
        CookbookDTO cookbookDTO = cookbookService.findByUrlId(urlId);
        cookbookDTO.getRecipes().sort(new RecipeDTOAscComparator());

        model.addAttribute("recipeDTOList", cookbookDTO.getRecipes());
        refreshUpdateCookbook(cookbookDTO, model, principal);

        return "cookbookDetails";
    }

    @GetMapping("/cookbook/myCookbook/{username}")
    protected String showMyCookbook(@PathVariable("username") String username,
                                    Model model,
                                    Principal principal) {
        List<RecipeDTO> recipeDTOList = recipeService.getRecipesByUsername(username);
        recipeDTOList.sort(new RecipeDTOAscComparator());
        SocialMealsUserDTO socialMealsUserDTO = socialMealsUserDetailService.getUserByUsername(username);

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
        setCookbookWithSocialMealsUser(cookbookDTO, principal);

        cookbookService.addNew(cookbookDTO);

        return "redirect:/MyKitchen";
    }

    @GetMapping("/cookbook/update/{urlId}")
    protected String showUpdateCookbookForm(@PathVariable("urlId") Long urlId,
                                    Model model) {
        CookbookDTO cookbookDTO = cookbookService.findByUrlId(urlId);
        if (cookbookDTO == null) {
            model.addAttribute("cookbookDTO", new CookbookDTO());
        } else {
            model.addAttribute("cookbookDTO", cookbookDTO);
        }
        return "cookbookUpdateForm";
    }

    @PostMapping("/cookbook/update")
    protected String saveExistingCookbook(@ModelAttribute("cookbookDTO") CookbookDTO cookbookDTO,
                                     BindingResult result,
                                     Principal principal) {
        if (result.hasErrors()) {
            return "redirect:/MyKitchen";
        }
        CookbookDTO oldCookbookDTO = cookbookService.findByUrlId(cookbookDTO.getUrlId());

        if (isItYours(principal, oldCookbookDTO.getSocialMealsUser())) {
            cookbookService.updateCookbook(cookbookDTO);
        }

        return "redirect:/MyKitchen";
    }

    @GetMapping("/cookbook/delete/{urlId}")
    protected String deleteCookbook(@PathVariable("urlId") Long urlId,
                                          Principal principal) {

        CookbookDTO cookbookDTO = cookbookService.findByUrlId(urlId);

        if (isItYours(principal, cookbookDTO.getSocialMealsUser())) {
            cookbookService.deleteCookbook(cookbookDTO);
        }

        return "redirect:/MyKitchen";
    }

    @GetMapping("/cookbook/add/{urlId}/{recipeUrlId}")
    protected String addRecipeToCookbook(@PathVariable("urlId") Long urlId,
                                         @PathVariable("recipeUrlId") Long recipeUrlId,
                                         Principal principal) {

        CookbookDTO cookbookDTO = cookbookService.findByUrlId(urlId);
        if (isItYours(principal, cookbookDTO.getSocialMealsUser())) {
            cookbookService.addRecipeDTO(cookbookDTO, recipeService.findByUrlId(recipeUrlId));
        }

        return "redirect:/recipes/" + recipeUrlId;
    }

     @GetMapping("/cookbook/remove/{urlId}/{recipeUrlId}")
     protected String removeRecipeFromCookbook(@PathVariable("urlId") Long urlId,
                                               @PathVariable("recipeUrlId") Long recipeUrlId,
                                               Principal principal) {
         CookbookDTO cookbookDTO = cookbookService.findByUrlId(urlId);
         if (isItYours(principal, cookbookDTO.getSocialMealsUser())) {
             cookbookService.removeRecipeDTO(cookbookDTO, recipeService.findByUrlId(recipeUrlId));
         }

         return "redirect:/cookbook/" + cookbookDTO.getUrlId();
     }

    @GetMapping(value = "/cookbook/recipe/search", params = {"cookbookid"})
    protected String searchRecipeInCookbook(Model model,
                                            @RequestParam String cookbookid,
                                            @RequestParam String keyword,
                                            Principal principal) {
        CookbookDTO cookbookDTO = cookbookService.findByUrlId(Long.parseLong(cookbookid));
        List<RecipeDTO> recipeDTOList = cookbookService.searchInCookbook(cookbookDTO, keyword);
        recipeDTOList.sort(new RecipeDTOAscComparator());

        model.addAttribute("recipeDTOList", recipeDTOList);
        refreshUpdateCookbook(cookbookDTO, model, principal);

        return "cookbookDetails";
    }

    @GetMapping(value = "/cookbook/recipe/search", params = {"cookbookuser"})
    protected String searchRecipeInMyCookbook(Model model,
                                              @RequestParam String keyword,
                                              @RequestParam String cookbookuser,
                                              Principal principal) {

        SocialMealsUserDTO socialMealsUserDTO = socialMealsUserDetailService.getUserByUsername(cookbookuser);
        List<RecipeDTO> recipeDTOList = cookbookService.searchInCookbook(socialMealsUserDTO, keyword);
        recipeDTOList.sort(new RecipeDTOAscComparator());

        model.addAttribute("ownRecipe", isItYours(principal, socialMealsUserDTO));
        model.addAttribute("socialMealsUserDTO", socialMealsUserDTO);
        model.addAttribute("recipeDTOList", recipeDTOList);

        return "myCookbook";
    }

    private void refreshUpdateCookbook(CookbookDTO cookbookDTO, Model model, Principal principal) {
        SocialMealsUserDTO socialMealsUserDTO =
                socialMealsUserDetailService.getUserByUsername(cookbookDTO.getSocialMealsUser().getUsername());

        model.addAttribute("cookbookDTO", cookbookDTO);
        model.addAttribute("socialMealsUserDTO", socialMealsUserDTO);
        model.addAttribute("ownRecipe", isItYours(principal, socialMealsUserDTO));
    }

    private boolean isItYours(Principal principal, SocialMealsUserDTO socialMealsUserDTO) {
        return principal.getName().equals(socialMealsUserDTO.getUsername());
    }

    private void setCookbookWithSocialMealsUser(CookbookDTO cookbookDTO, Principal principal) {
        SocialMealsUserDTO socialMealsUserDTO = socialMealsUserDetailService.getUserByUsername(principal.getName());
        if (socialMealsUserDTO != null) {
            cookbookDTO.setSocialMealsUser(socialMealsUserDTO);
        }
    }
}