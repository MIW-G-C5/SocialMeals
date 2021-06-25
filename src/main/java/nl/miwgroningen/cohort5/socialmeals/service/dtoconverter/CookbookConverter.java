package nl.miwgroningen.cohort5.socialmeals.service.dtoconverter;

import nl.miwgroningen.cohort5.socialmeals.dto.CookbookDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.SocialMealsUserDTO;
import nl.miwgroningen.cohort5.socialmeals.model.Cookbook;
import nl.miwgroningen.cohort5.socialmeals.model.Recipe;
import nl.miwgroningen.cohort5.socialmeals.model.SocialMealsUser;
import nl.miwgroningen.cohort5.socialmeals.service.RecipeService;
import nl.miwgroningen.cohort5.socialmeals.service.implementation.SocialMealsUserDetailService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Britt van Mourik
 * Converts Cookbooks to CookbookDTO's
 */

public class CookbookConverter {

    private SocialMealsUserDetailService socialMealsUserDetailService;

    private SocialMealsUserConverter socialMealsUserConverter;
    private RecipeConverter recipeConverter;

    public CookbookConverter(SocialMealsUserDetailService socialMealsUserDetailService) {
        this.socialMealsUserDetailService = socialMealsUserDetailService;

        this.socialMealsUserConverter = new SocialMealsUserConverter();
        this.recipeConverter = new RecipeConverter(socialMealsUserDetailService);
    }

    public CookbookDTO toDTO(Cookbook cookbook) {

        SocialMealsUserDTO user = socialMealsUserConverter.toDTO(cookbook.getSocialMealsUser());
        List<RecipeDTO> recipeDTOs = recipeConverter.toListDTO(List.copyOf(cookbook.getRecipeLikes()));

        return new CookbookDTO(cookbook.getUrlId(), cookbook.getCookbookName(), user, recipeDTOs);
    }

    public List<CookbookDTO> toListDTO(List<Cookbook> cookbookList){

        List<CookbookDTO> cookbookDTOs = new ArrayList<>();

        for (Cookbook cookbook : cookbookList){
            cookbookDTOs.add(toDTO(cookbook));
        }

        return cookbookDTOs;
    }

    public Cookbook fromNewCookbookDTO(CookbookDTO cookbookDTO) {
        SocialMealsUser socialMealsUser = socialMealsUserDetailService.getUserByDTO(cookbookDTO.getSocialMealsUser());

        Cookbook cookbook = new Cookbook();
        cookbook.setCookbookName(cookbookDTO.getCookbookName());
        cookbook.setSocialMealsUser(socialMealsUser);
        cookbook.setRecipeLikes(new HashSet<>());
        cookbook.setUrlId(cookbookDTO.getUrlId());

        return cookbook;
    }

}
