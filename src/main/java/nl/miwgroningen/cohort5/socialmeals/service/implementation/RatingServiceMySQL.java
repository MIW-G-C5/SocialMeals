package nl.miwgroningen.cohort5.socialmeals.service.implementation;

import nl.miwgroningen.cohort5.socialmeals.dto.RatingDTO;
import nl.miwgroningen.cohort5.socialmeals.model.Rating;
import nl.miwgroningen.cohort5.socialmeals.model.Recipe;
import nl.miwgroningen.cohort5.socialmeals.model.SocialMealsUser;
import nl.miwgroningen.cohort5.socialmeals.repository.RatingRepository;
import nl.miwgroningen.cohort5.socialmeals.service.RatingService;
import nl.miwgroningen.cohort5.socialmeals.service.RecipeService;
import nl.miwgroningen.cohort5.socialmeals.service.dtoconverter.RatingConverter;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Wessel van Dommelen <w.r.van.dommelen@st.hanze.nl>
 * Collects and stores Ratings in the MySQL Database
 */

@Service
public class RatingServiceMySQL implements RatingService {

    private final RatingRepository ratingRepository;

    private final RecipeService recipeService;
    private final SocialMealsUserDetailService socialMealsUserDetailService;

    private final RatingConverter ratingConverter;


    public RatingServiceMySQL(RatingRepository ratingRepository, RecipeService recipeService, SocialMealsUserDetailService socialMealsUserDetailService) {
        this.ratingRepository = ratingRepository;
        this.recipeService = recipeService;
        this.socialMealsUserDetailService = socialMealsUserDetailService;

        ratingConverter = new RatingConverter();
    }

    @Override
    public RatingDTO addNew(RatingDTO ratingDTO) {
        Recipe recipe = recipeService.getRecipeByRecipeDTO(ratingDTO.getRecipeDTO());
        SocialMealsUser socialMealsUser = socialMealsUserDetailService.getUserByDTO(ratingDTO.getSocialMealsUserDTO());
        Rating rating = ratingConverter.fromDTO(ratingDTO, recipe, socialMealsUser);

        Rating oldRating = findRatingByUserAndRecipe(socialMealsUser, recipe);

        if (oldRating == null) {
            ratingRepository.save(rating);
        } else {
            oldRating.setStars(ratingDTO.getStars());
            ratingRepository.save(oldRating);
        }

        return ratingDTO;
    }

    private Rating findRatingByUserAndRecipe(SocialMealsUser socialMealsUser, Recipe recipe) {
        Optional<Rating> rating = ratingRepository.findRatingBySocialMealsUserAndRecipe(socialMealsUser, recipe);

        return rating.orElse(null);
    }
}
