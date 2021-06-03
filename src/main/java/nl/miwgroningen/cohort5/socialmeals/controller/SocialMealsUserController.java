package nl.miwgroningen.cohort5.socialmeals.controller;

import nl.miwgroningen.cohort5.socialmeals.model.SocialMealsUser;
import nl.miwgroningen.cohort5.socialmeals.repository.SocialMealsUserRepository;
import nl.miwgroningen.cohort5.socialmeals.service.implementation.SocialMealsUserDetailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Britt van Mourik
 */


@Controller
public class SocialMealsUserController {

    private SocialMealsUserRepository socialMealsUserRepository;

    private SocialMealsUserDetailService socialMealsUserDetailService;

    public SocialMealsUserController(SocialMealsUserRepository socialMealsUserRepository) {
        this.socialMealsUserRepository = socialMealsUserRepository;
        socialMealsUserDetailService = new SocialMealsUserDetailService(socialMealsUserRepository);
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
}
