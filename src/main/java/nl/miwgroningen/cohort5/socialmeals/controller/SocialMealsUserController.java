package nl.miwgroningen.cohort5.socialmeals.controller;

import nl.miwgroningen.cohort5.socialmeals.dto.SocialMealsUserDTO;
import nl.miwgroningen.cohort5.socialmeals.model.SocialMealsUser;
import nl.miwgroningen.cohort5.socialmeals.service.implementation.SocialMealsUserDetailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


/**
 * Britt van Mourik
 *
 * Controls the view of the registrationform
 */


@Controller
public class SocialMealsUserController {

    private SocialMealsUserDetailService socialMealsUserDetailService;

    public SocialMealsUserController(SocialMealsUserDetailService socialMealsUserDetailService) {
        this.socialMealsUserDetailService = socialMealsUserDetailService;
    }

    @GetMapping("/user/new")
    protected String showUserForm(Model model) {
        model.addAttribute("user", new SocialMealsUser());
        return "userForm";
    }

    @PostMapping("/user/new")
    protected String saveOrUpdateUser(@ModelAttribute("user") SocialMealsUser user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "redirect:/user/new";
        }

        SocialMealsUserDTO socialMealsUserDTO = socialMealsUserDetailService.getUserByUsername(user.getUsername());
        if (socialMealsUserDTO != null) {
            return showUserFormWithNotificationUserExists(model, socialMealsUserDTO);
        }

        socialMealsUserDetailService.addSocialMealsUser(user.getUsername(), user.getPassword());
        return "redirect:/";
    }

    private String showUserFormWithNotificationUserExists(Model model, SocialMealsUserDTO socialMealsUserDTO) {
        model.addAttribute("user", new SocialMealsUser());
        model.addAttribute("existingUsername", socialMealsUserDTO.getUsername());
        return "userForm";
    }


}
