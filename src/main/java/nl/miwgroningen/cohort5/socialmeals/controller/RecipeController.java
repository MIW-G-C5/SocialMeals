package nl.miwgroningen.cohort5.socialmeals.controller;


import nl.miwgroningen.cohort5.socialmeals.comparator.RecipeDTOAscComparator;
import nl.miwgroningen.cohort5.socialmeals.comparator.RecipeDTODescComparator;
import nl.miwgroningen.cohort5.socialmeals.dto.IngredientDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.IngredientRecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.SocialMealsUserDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.CookbookDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.stateKeeper.SortedRecipesStateKeeper;
import nl.miwgroningen.cohort5.socialmeals.model.Ingredient;
import nl.miwgroningen.cohort5.socialmeals.service.IngredientService;
import nl.miwgroningen.cohort5.socialmeals.service.RecipeService;
import nl.miwgroningen.cohort5.socialmeals.service.CookbookService;
import nl.miwgroningen.cohort5.socialmeals.service.implementation.SocialMealsUserDetailService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Wessel van Dommelen <w.r.van.dommelen@st.hanze.nl>
 *
 * Controls the view of the recipes overview page and creating and updating pages
 */

@Controller
@SessionAttributes("sortedRecipesStateKeeper")
public class RecipeController {

    private RecipeService recipeService;
    private IngredientService ingredientService;
    private SocialMealsUserDetailService socialMealsUserDetailService;
    private CookbookService cookbookService;

    public RecipeController(RecipeService recipeService, IngredientService ingredientService,
                            SocialMealsUserDetailService socialMealsUserDetailService,
                            CookbookService cookbookService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.socialMealsUserDetailService = socialMealsUserDetailService;
        this.cookbookService = cookbookService;
    }

    @ModelAttribute("sortedRecipesStateKeeper")
    public SortedRecipesStateKeeper stateKeeper(){
        return new SortedRecipesStateKeeper();
    }

    @GetMapping({"/", "/recipes"})
    protected String showRecipes(
            @ModelAttribute("sortedRecipesStateKeeper") SortedRecipesStateKeeper sortedRecipesStateKeeper,
            Model model) {

        List<RecipeDTO> recipeDTOList = recipeService.getAll();
        sortedRecipesStateKeeper.setSortedRecipes(recipeDTOList);

        Collections.sort(sortedRecipesStateKeeper.getSortedRecipes(), new RecipeDTOAscComparator());

        model.addAttribute("recipeList", sortedRecipesStateKeeper.getSortedRecipes());


        return "recipeOverview";
    }

    @GetMapping("/recipes/asc")
    protected String sortAscRecipes(
            @SessionAttribute("sortedRecipesStateKeeper") SortedRecipesStateKeeper sortedRecipesStateKeeper,
            Model model){

        Collections.sort(sortedRecipesStateKeeper.getSortedRecipes(), new RecipeDTOAscComparator());

        model.addAttribute("recipeList", sortedRecipesStateKeeper.getSortedRecipes());
        return "recipeOverview";
    }

    @GetMapping("/recipes/desc")
    protected String sortDescRecipes(
            @SessionAttribute("sortedRecipesStateKeeper") SortedRecipesStateKeeper sortedRecipesStateKeeper,
            Model model){

        Collections.sort(sortedRecipesStateKeeper.getSortedRecipes(), new RecipeDTODescComparator());

        model.addAttribute("recipeList", sortedRecipesStateKeeper.getSortedRecipes());
        return "recipeOverview";
    }

    @GetMapping("/recipes/{urlId}")
    protected String showRecipeDetails(@PathVariable("urlId") Long urlId,
                                       Model model,
                                       Principal principal) {

        RecipeDTO recipe = recipeService.findByUrlId(urlId);

        if (recipe == null) {
            return "redirect:/recipes";
        }

        boolean loggedInUser = principal != null;
        if (loggedInUser) {
            SocialMealsUserDTO socialMealsUserDTO = socialMealsUserDetailService.getUserByUsername(principal.getName());
            List<CookbookDTO> cookbookDTOList = cookbookService.getCookbooksByUser(socialMealsUserDTO);
            model.addAttribute("cookbookList", cookbookDTOList);
        }

        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("recipe", recipe);
        model.addAttribute("ingredientRecipes", recipeService.getIngredientRecipesByRecipeName(recipe.getRecipeName()));
        return "recipeDetails";
    }

    @GetMapping("/recipe/new")
    protected String showRecipeForm(Model model) {
        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.getSteps().add("");
        model.addAttribute("recipeDTO", recipeDTO);
        return "recipeForm";
    }

    @PostMapping(value = "/recipe/new/newRecipe", params = "add")
    protected String updateShowRecipeForm(Model model,
                                          @ModelAttribute("recipeDTO") RecipeDTO recipeDTOSessionObject,
                                          BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/";
        }
        recipeDTOSessionObject.getSteps().add("");
        model.addAttribute("recipeDTO", recipeDTOSessionObject);
        return "recipeForm";
    }

    @PostMapping(value = "/recipe/new/newRecipe", params = "save")
    protected String saveRecipe(Model model,
                                @ModelAttribute("recipeDTOSessionObject") RecipeDTO recipeDTOSessionObject,
                                Principal principal,
                                BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/";
        }
        SocialMealsUserDTO socialMealsUserDTO = socialMealsUserDetailService.getUserByUsername(principal.getName());
        if (socialMealsUserDTO != null) {
            recipeDTOSessionObject.setSocialMealsUserDTO(socialMealsUserDTO);
        }

        recipeDTOSessionObject.setSteps(removeEmptySteps(recipeDTOSessionObject.getSteps()));

        try {
            recipeService.addNew(recipeDTOSessionObject);
        } catch (DataIntegrityViolationException error) {
            return createRecipeFormWithNotificationRecipeExists(model, recipeDTOSessionObject);
        }

        return "redirect:/recipe/update/" + recipeService.findByRecipeName(recipeDTOSessionObject.getRecipeName()).getUrlId();
    }


    @PostMapping(value = "/recipe/new/newRecipe", params = "delete")
    protected String deleteStepFromRecipeForm(Model model,
                                              @ModelAttribute("recipeDTO") RecipeDTO recipeDTOSessionObject,
                                              @RequestParam("stepIndex") int stepIndex,
                                              BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/";
        }

        recipeDTOSessionObject.getSteps().remove(stepIndex);
        model.addAttribute("recipeDTO", recipeDTOSessionObject);
        return "recipeForm";
    }


    @GetMapping("/recipe/update/{urlId}")
    protected String showUpdateRecipe(@PathVariable("urlId") Long urlId, Model model, Principal principal) {
        RecipeDTO recipeDTO = recipeService.findByUrlId(urlId);

        if (recipeDTO == null || recipeUserDoesNotMatchCurrentUser(principal, recipeDTO)) {
            return "redirect:/MyKitchen";
        }

       refreshUpdateRecipe(recipeDTO.getRecipeName(), recipeDTO, model);

        return "updateRecipeForm";
    }

    @PostMapping(value = "/recipe/update/{urlId}", params = "save")
    protected String updateRecipe(@PathVariable("urlId") Long urlId,
                                  @ModelAttribute("recipeDTO") RecipeDTO recipeDTO,
                                  Model model,
                                  BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/MyKitchen";
        }

        recipeDTO.setSteps(removeEmptySteps(recipeDTO.getSteps()));

        try {
            recipeService.updateRecipe(recipeService.findByUrlId(urlId), recipeDTO);
        } catch (DataIntegrityViolationException error) {
            return createRecipeUpdateFormWithNotificationRecipeExists(model, recipeDTO, recipeDTO.getRecipeName());
        }

        return "redirect:/recipe/update/" + urlId;
    }

    @PostMapping(value = "/recipe/update/{urlId}", params = "add")
    protected String addStepToUpdateRecipe(@PathVariable("urlId") Long urlId,
                                           @ModelAttribute("recipeDTO") RecipeDTO recipeDTO,
                                           Model model,
                                           BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/MyKitchen";
        }

        recipeDTO.getSteps().add("");
        refreshUpdateRecipe(recipeDTO.getRecipeName(), recipeDTO, model);

        return "updateRecipeForm";
    }

    @PostMapping(value = "/recipe/update/{urlId}", params = "delete")
    protected String deleteStepFromUpdateRecipe(
            @PathVariable("urlId") Long urlId,
            @ModelAttribute("recipeDTO") RecipeDTO recipeDTO,
            @RequestParam("stepIndex") int stepIndex,
            Model model,
            BindingResult result) {

        if (result.hasErrors()) {
            return "redirect:/MyKitchen";
        }

        recipeDTO.getSteps().remove(stepIndex);

        refreshUpdateRecipe(recipeDTO.getRecipeName(), recipeDTO, model);

        return "updateRecipeForm";
    }

    @PostMapping(value = "/recipe/update/{urlId}/addingredient")
    protected String addIngredient(@ModelAttribute("ingredientRecipeDTO") IngredientRecipeDTO ingredientRecipeDTO,
                                   @PathVariable("urlId") Long urlId,
                                   @RequestParam("ingredientName") String ingredientName,
                                   BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/";
        }

        IngredientDTO ingredientDTO = ingredientService.findByIngredientName(ingredientName);
        if (ingredientDTO == null) {
            ingredientDTO = ingredientService.addNew(new IngredientDTO(ingredientName));
        }

        try {
            ingredientRecipeDTO.setRecipeDTO(recipeService.findByUrlId(urlId));
            ingredientRecipeDTO.setIngredientDTO(ingredientDTO);
            recipeService.addIngredientToRecipe(ingredientRecipeDTO);
        } catch (NullPointerException error) {
            System.err.println(error.getMessage());
        }

        return "redirect:/recipe/update/" + urlId;
    }


    @GetMapping("/recipe/delete/{urlId}/{ingredientName}")
    protected String deleteRecipe(@PathVariable("urlId") Long urlId,
                                  @PathVariable("ingredientName") String ingredientName) {

        RecipeDTO recipeDTO = recipeService.findByUrlId(urlId);

        try {
            recipeService.deleteIngredientFromRecipe(recipeService.getIngredientRecipeByNames(ingredientName, recipeDTO.getRecipeName()));
        } catch (NullPointerException error) {
            System.err.println(error.getMessage());
        }

        return "redirect:/recipe/update/" + urlId;

    }

    @GetMapping(value = "/recipes/search")
    protected String searchRecipe(@SessionAttribute("sortedRecipesStateKeeper") SortedRecipesStateKeeper sortedRecipesStateKeeper,
                                  Model model, @RequestParam String keyword) {
        List<String> searchResults = recipeService.search(keyword);
        List<RecipeDTO> sortedRecipes = new ArrayList<>();

        for (String searchResult : searchResults) {
            sortedRecipes.add(recipeService.findByRecipeName(searchResult));
        }

        sortedRecipesStateKeeper.setSortedRecipes(sortedRecipes);

        model.addAttribute("recipeList", sortedRecipesStateKeeper.getSortedRecipes());

        return "recipeOverview";
    }

    @RequestMapping(value="/recipe/update/ingredientAutocomplete")
    @ResponseBody
    public List<String> ingredientAutocomplete(@RequestParam(value="term") String keyword,
                                               @RequestParam(value="recipeName") String recipeName) {
        List<String> searchIngredients = ingredientService.search(keyword);
        List<String> remainingIngredients = recipeService.getRemainingIngredientsByRecipeName(recipeName)
                .stream().map(IngredientDTO::getIngredientName).collect(Collectors.toList());
        return searchIngredients.stream().filter(remainingIngredients::contains).collect(Collectors.toList());
    }

    private void refreshUpdateRecipe
            (String recipeName,
             RecipeDTO recipeDTO,
             Model model) {

        model.addAttribute("recipeDTO", recipeDTO);
        model.addAttribute("ingredientRecipeDTO", new IngredientRecipeDTO());
        model.addAttribute("presentIngredientsRecipes", recipeService.getIngredientRecipesByRecipeName(recipeName));
        model.addAttribute("remainingIngredients", recipeService.getRemainingIngredientsByRecipeName(recipeName));
    }

    private boolean recipeUserDoesNotMatchCurrentUser(Principal principal, RecipeDTO recipeDTO) {
        SocialMealsUserDTO currentUser = socialMealsUserDetailService.getUserByUsername(principal.getName());
        return !currentUser.equals(recipeDTO.getSocialMealsUserDTO());
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

    private List<String> removeEmptySteps(List<String> steps) {
        return steps.stream().filter(i -> !i.isEmpty()).collect(Collectors.toList());
    }
}
