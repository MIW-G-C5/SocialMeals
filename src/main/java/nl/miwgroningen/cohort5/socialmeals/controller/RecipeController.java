package nl.miwgroningen.cohort5.socialmeals.controller;


import nl.miwgroningen.cohort5.socialmeals.comparator.RecipeDTOAscComparator;
import nl.miwgroningen.cohort5.socialmeals.comparator.RecipeDTODescComparator;
import nl.miwgroningen.cohort5.socialmeals.dto.IngredientDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.IngredientRecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.SocialMealsUserDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.CookbookDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.stateKeeper.SortedRecipesStateKeeper;
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
@SessionAttributes({"sortedRecipesStateKeeper", "recipeStateKeeper"})
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
    public SortedRecipesStateKeeper sortedRecipeStateKeeper(){
        return new SortedRecipesStateKeeper();
    }

    @ModelAttribute("recipeStateKeeper")
    public RecipeDTO recipeStateKeeper() {
        return new RecipeDTO();
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

    @GetMapping("/recipes/{recipeName}")
    protected String showRecipeDetails(@PathVariable("recipeName") String recipeName,
                                       Model model,
                                       Principal principal) {
        RecipeDTO recipe = recipeService.findByRecipeName(recipeName);
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
        model.addAttribute("ingredientRecipes", recipeService.getIngredientRecipesByRecipeName(recipeName));
        return "recipeDetails";
    }

    @GetMapping("/recipe/new")
    protected String showRecipeForm(@ModelAttribute("recipeStateKeeper") RecipeDTO recipeStateKeeper,
                                    Model model) {
        recipeStateKeeper = new RecipeDTO();
        recipeStateKeeper.getSteps().add("");

        model.addAttribute("recipeDTO", recipeStateKeeper);
        return "recipeForm";
    }

    @PostMapping(value = "/recipe/new/newRecipe", params = "add")
    protected String updateShowRecipeForm(Model model,
                                          @ModelAttribute("recipeDTO") RecipeDTO recipeDTO,
                                          @SessionAttribute("recipeStateKeeper") RecipeDTO recipeStateKeeper,
                                          BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/";
        }
        setRecipeKeeperValuesWithRecipeDTOValues(recipeStateKeeper, recipeDTO);
        recipeStateKeeper.getSteps().add("");

        model.addAttribute("recipeDTO", recipeStateKeeper);
        return "recipeForm";
    }

    @PostMapping(value = "/recipe/new/newRecipe", params = "save")
    protected String saveRecipe(Model model,
                                @ModelAttribute("recipeDTO") RecipeDTO recipeDTO,
                                Principal principal,
                                BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/";
        }
        SocialMealsUserDTO socialMealsUserDTO = socialMealsUserDetailService.getUserByUsername(principal.getName());
        if (socialMealsUserDTO != null) {
            recipeDTO.setSocialMealsUserDTO(socialMealsUserDTO);
        }

        recipeDTO.setSteps(removeEmptySteps(recipeDTO.getSteps()));

        try {
            recipeService.addNew(recipeDTO);
        } catch (DataIntegrityViolationException error) {
            return createRecipeFormWithNotificationRecipeExists(model, recipeDTO);
        }

        return "redirect:/recipe/update/" + stringURLify(recipeDTO.getRecipeName());
    }

    @GetMapping(value = "/recipe/deleteStep/{iterIndex}")
    protected String deleteStepRecipe(@PathVariable("iterIndex") int iterIndex,
                                      @SessionAttribute("recipeStateKeeper") RecipeDTO recipeStateKeeper,
                                      Model model){

        recipeStateKeeper.getSteps().remove(iterIndex);
        RecipeDTO existingRecipe = recipeService.findByRecipeName(recipeStateKeeper.getRecipeName());

        if (existingRecipe == null) {
            model.addAttribute("recipeDTO", recipeStateKeeper);
            return "recipeForm";
        }
        existingRecipe.setSteps(recipeStateKeeper.getSteps());
        refreshUpdateRecipe(existingRecipe, model);
        return "updateRecipeForm";

    }

    @GetMapping("/recipe/update/{recipeName}")
    protected String showUpdateRecipe(@PathVariable("recipeName") String recipeName,
                                      @ModelAttribute("recipeStateKeeper") RecipeDTO recipeStateKeeper,
                                      Model model,
                                      Principal principal) {

        RecipeDTO existingRecipe =  recipeService.findByRecipeName(recipeName);

        setRecipeKeeperValuesWithRecipeDTOValues(recipeStateKeeper, existingRecipe);


        if (recipeStateKeeper == null || recipeUserDoesNotMatchCurrentUser(principal, recipeStateKeeper)) {
            return "redirect:/MyKitchen";
        }

       refreshUpdateRecipe(recipeStateKeeper, model);

        return "updateRecipeForm";
    }

    @PostMapping(value = "/recipe/update/updateSteps", params = "save")
    protected String updateRecipe(@ModelAttribute("recipeDTO") RecipeDTO recipeDTO,
                                  Model model,
                                  BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/MyKitchen";
        }

        recipeDTO.setSteps(removeEmptySteps(recipeDTO.getSteps()));

        try {
            recipeService.updateRecipe(recipeService.findByRecipeName(recipeDTO.getRecipeName()), recipeDTO);
        } catch (DataIntegrityViolationException error) {
            return createRecipeUpdateFormWithNotificationRecipeExists(model, recipeDTO, recipeDTO.getRecipeName());
        }

        return "redirect:/recipe/update/" + stringURLify(recipeDTO.getRecipeName());
    }

    @PostMapping(value = "/recipe/update/updateSteps", params = "add")
    protected String addStepToUpdateRecipe(@ModelAttribute("recipeDTO") RecipeDTO recipeDTO,
                                           @SessionAttribute("recipeStateKeeper") RecipeDTO recipeStateKeeper,
                                           Model model,
                                           BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/MyKitchen";
        }

        setRecipeKeeperValuesWithRecipeDTOValues(recipeStateKeeper, recipeDTO);
        recipeStateKeeper.getSteps().add("");

        refreshUpdateRecipe(recipeStateKeeper, model);
        return "updateRecipeForm";
    }


    @PostMapping(value = "/recipe/update/{recipeName}/addingredient")
    protected String addIngredient(@ModelAttribute("ingredientRecipeDTO") IngredientRecipeDTO ingredientRecipeDTO,
                                   @PathVariable("recipeName") String recipeName,
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
            ingredientRecipeDTO.setRecipeDTO(recipeService.findByRecipeName(recipeName));
            ingredientRecipeDTO.setIngredientDTO(ingredientDTO);
            recipeService.addIngredientToRecipe(ingredientRecipeDTO);
        } catch (NullPointerException error) {
            System.err.println(error.getMessage());
        }

        return "redirect:/recipe/update/" + stringURLify(recipeName);
    }


    @GetMapping("/recipe/delete/{recipeName}/{ingredientName}")
    protected String deleteRecipe(@PathVariable("recipeName") String recipeName,
                                  @PathVariable("ingredientName") String ingredientName) {

        try {
            recipeService.deleteIngredientFromRecipe(recipeService.getIngredientRecipeByNames(ingredientName, recipeName));
        } catch (NullPointerException error) {
            System.err.println(error.getMessage());
        }

        return "redirect:/recipe/update/" + stringURLify(recipeName);

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

    private void refreshUpdateRecipe(@ModelAttribute("recipeDTO") RecipeDTO recipeDTO, Model model) {

        model.addAttribute("recipeDTO", recipeDTO);
        model.addAttribute("ingredientRecipeDTO", new IngredientRecipeDTO());
        model.addAttribute("presentIngredientsRecipes", recipeService.getIngredientRecipesByRecipeName(recipeDTO.getRecipeName()));
        model.addAttribute("remainingIngredients", recipeService.getRemainingIngredientsByRecipeName(recipeDTO.getRecipeName()));
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

    private void setRecipeKeeperValuesWithRecipeDTOValues(RecipeDTO recipeStateKeeper, RecipeDTO recipeDTO) {
        recipeStateKeeper.setRecipeName(recipeDTO.getRecipeName());
        recipeStateKeeper.setSteps(recipeDTO.getSteps());
        recipeStateKeeper.setSocialMealsUserDTO(recipeDTO.getSocialMealsUserDTO());
    }
}
