package nl.miwgroningen.cohort5.socialmeals.controller;

import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.SocialMealsUserDTO;
import nl.miwgroningen.cohort5.socialmeals.model.SocialMealsUser;
import nl.miwgroningen.cohort5.socialmeals.repository.IngredientRecipeRepository;
import nl.miwgroningen.cohort5.socialmeals.repository.IngredientRepository;
import nl.miwgroningen.cohort5.socialmeals.repository.RecipeRepository;
import nl.miwgroningen.cohort5.socialmeals.repository.SocialMealsUserRepository;
import nl.miwgroningen.cohort5.socialmeals.service.RecipeService;
import nl.miwgroningen.cohort5.socialmeals.service.dtoconverter.IngredientRecipeConverter;
import nl.miwgroningen.cohort5.socialmeals.service.implementation.RecipeServiceMySQL;
import nl.miwgroningen.cohort5.socialmeals.service.implementation.SocialMealsUserDetailService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;

/**
 * Britt van Mourik
 */


@Controller
public class SocialMealsUserController {


    private SocialMealsUserDetailService socialMealsUserDetailService;
    private RecipeService recipeService;

    public SocialMealsUserController(SocialMealsUserDetailService socialMealsUserDetailService,
                                     RecipeService recipeService) {
        this.socialMealsUserDetailService = socialMealsUserDetailService;
        this.recipeService = recipeService;
    }

    @GetMapping("/user/new")
    protected  String showUserForm(Model model){
        model.addAttribute("user", new SocialMealsUser());
        return "userForm";
    }

    @PostMapping("/user/new")
    protected String saveOrUpdateUser(@ModelAttribute("user") SocialMealsUser user, BindingResult result){
        if (!result.hasErrors()){
            socialMealsUserDetailService.addSocialMealsUser(user.getUsername(), user.getPassword());
        }
        return "redirect:/";
    }

    @GetMapping("/MyKitchen/{username}")
    protected String showUserRecipes(@PathVariable("username") String username, Model model) {

        SocialMealsUserDTO userDTO = socialMealsUserDetailService.getUserByUsername(username);

        if (userDTO == null) {
            return "redirect:/";
        }

        model.addAttribute("user", userDTO);
        model.addAttribute("userRecipes", recipeService.getRecipesByUsername(username));
        return "myKitchen";
    }

}
