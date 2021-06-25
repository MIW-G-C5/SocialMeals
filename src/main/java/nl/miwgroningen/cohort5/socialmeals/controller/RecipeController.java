package nl.miwgroningen.cohort5.socialmeals.controller;


import nl.miwgroningen.cohort5.socialmeals.comparator.RecipeDTOAscComparator;
import nl.miwgroningen.cohort5.socialmeals.comparator.RecipeDTODescComparator;
import nl.miwgroningen.cohort5.socialmeals.dto.IngredientDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.IngredientRecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.SocialMealsUserDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.CookbookDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.stateKeeper.SortedRecipesStateKeeper;
import nl.miwgroningen.cohort5.socialmeals.model.IngredientRecipe;
import nl.miwgroningen.cohort5.socialmeals.service.IngredientService;
import nl.miwgroningen.cohort5.socialmeals.service.RecipeService;
import nl.miwgroningen.cohort5.socialmeals.service.CookbookService;
import nl.miwgroningen.cohort5.socialmeals.service.mocks.SocialMealsUserDetailService;
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
        model.addAttribute("ingredientRecipes", recipeService.getIngredientRecipesByRecipeUrlId(urlId));
        return "recipeDetails";
    }

    @GetMapping("/recipe/new")
    protected String showRecipeForm(@ModelAttribute("recipeStateKeeper") RecipeDTO recipeStateKeeper,
                                    Model model) {

        RecipeDTO recipeDTO = new RecipeDTO();
        setRecipeKeeperValuesWithRecipeDTOValues(recipeStateKeeper, recipeDTO);
        recipeStateKeeper.setUrlId(recipeDTO.getUrlId());
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

        return "redirect:/recipe/update/" + recipeDTO.getUrlId();
    }

    @GetMapping(value = "/recipe/deleteStep/{iterIndex}")
    protected String deleteStepRecipe(@PathVariable("iterIndex") int iterIndex,
                                      @SessionAttribute("recipeStateKeeper") RecipeDTO recipeStateKeeper,
                                      Model model){

        recipeStateKeeper.getSteps().remove(iterIndex);
        RecipeDTO existingRecipe = recipeService.findByUrlId(recipeStateKeeper.getUrlId());

        if (existingRecipe == null) {
            model.addAttribute("recipeDTO", recipeStateKeeper);
            return "recipeForm";
        }

        existingRecipe.setSteps(recipeStateKeeper.getSteps());
        refreshUpdateRecipe(existingRecipe, model);
        return "updateRecipeForm";
    }

    @GetMapping("/recipe/update/{urlId}")
    protected String showUpdateRecipe(@PathVariable("urlId") Long urlId,
                                      @ModelAttribute("recipeStateKeeper") RecipeDTO recipeStateKeeper,
                                      Model model,
                                      Principal principal) {

        RecipeDTO existingRecipe =  recipeService.findByUrlId(urlId);

        setRecipeKeeperValuesWithRecipeDTOValues(recipeStateKeeper, existingRecipe);


        if (recipeStateKeeper == null || recipeUserDoesNotMatchCurrentUser(principal, recipeStateKeeper)) {
            return "redirect:/MyKitchen";
        }

       refreshUpdateRecipe(recipeStateKeeper, model);

        return "updateRecipeForm";
    }

    @PostMapping(value = "/recipe/update/updateSteps", params = "save")
    protected String updateRecipe(@ModelAttribute("recipeDTO") RecipeDTO recipeDTO,
                                  @SessionAttribute("recipeStateKeeper") RecipeDTO recipeStateKeeper,
                                  Model model,
                                  BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/MyKitchen";
        }

        recipeDTO.setSteps(removeEmptySteps(recipeDTO.getSteps()));
        RecipeDTO oldRecipe = recipeService.findByUrlId(recipeStateKeeper.getUrlId());

        try {
            recipeService.updateRecipe(oldRecipe, recipeDTO);
        } catch (DataIntegrityViolationException error) {
            return createRecipeUpdateFormWithNotificationRecipeExists(model, recipeDTO, recipeDTO.getUrlId());
        }

        return "redirect:/recipe/update/" + recipeStateKeeper.getUrlId();
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
            IngredientRecipe ingredientRecipe = recipeService.getIngredientRecipeByNameAndUrlId(ingredientName, urlId);
            recipeService.deleteIngredientFromRecipe(ingredientRecipe);
        } catch (NullPointerException error) {
            System.err.println(error.getMessage());
        }

        return "redirect:/recipe/update/" + urlId;

    }

    @GetMapping(value = "/recipes/search")
    protected String searchRecipe(@SessionAttribute("sortedRecipesStateKeeper") SortedRecipesStateKeeper sortedRecipesStateKeeper,
                                  Model model, @RequestParam String keyword) {
        List<Long> searchResults = recipeService.search(keyword);
        List<RecipeDTO> sortedRecipes = new ArrayList<>();

        for (Long searchResult : searchResults) {
            sortedRecipes.add(recipeService.findByUrlId(searchResult));
        }

        sortedRecipesStateKeeper.setSortedRecipes(sortedRecipes);

        model.addAttribute("recipeList", sortedRecipesStateKeeper.getSortedRecipes());

        return "recipeOverview";
    }

    @RequestMapping(value="/recipe/update/ingredientAutocomplete")
    @ResponseBody
    public List<String> ingredientAutocomplete(@RequestParam(value="term") String keyword,
                                               @RequestParam(value="urlId") Long urlId) {
        List<String> searchIngredients = ingredientService.search(keyword);
        List<String> remainingIngredients = recipeService.getRemainingIngredientsByUrlId(urlId)
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

    private void refreshUpdateRecipe(RecipeDTO recipeDTO, Model model) {

        model.addAttribute("recipeDTO", recipeDTO);
        model.addAttribute("ingredientRecipeDTO", new IngredientRecipeDTO());
        model.addAttribute("presentIngredientsRecipes", recipeService.getIngredientRecipesByRecipeUrlId(recipeDTO.getUrlId()));
        model.addAttribute("remainingIngredients", recipeService.getRemainingIngredientsByUrlId(recipeDTO.getUrlId()));
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

    private String createRecipeUpdateFormWithNotificationRecipeExists(Model model, RecipeDTO duplicateRecipeDTO, Long urlId) {
        model.addAttribute("existingRecipe", duplicateRecipeDTO);

        RecipeDTO recipeDTO = recipeService.findByUrlId(urlId);
        model.addAttribute("recipeDTO", recipeDTO);
        model.addAttribute("ingredientRecipeDTO", new IngredientRecipeDTO());
        model.addAttribute("presentIngredientsRecipes", recipeService.getIngredientRecipesByRecipeUrlId(urlId));
        model.addAttribute("remainingIngredients", recipeService.getRemainingIngredientsByUrlId(urlId));

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
