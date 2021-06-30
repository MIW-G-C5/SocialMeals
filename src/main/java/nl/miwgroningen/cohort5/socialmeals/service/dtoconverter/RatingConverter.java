package nl.miwgroningen.cohort5.socialmeals.service.dtoconverter;

import nl.miwgroningen.cohort5.socialmeals.dto.RatingDTO;
import nl.miwgroningen.cohort5.socialmeals.model.Rating;
import nl.miwgroningen.cohort5.socialmeals.model.Recipe;
import nl.miwgroningen.cohort5.socialmeals.model.SocialMealsUser;

/**
 * @author Wessel van Dommelen <w.r.van.dommelen@st.hanze.nl>
 * Converts RatingDTO's to Ratings and vice versa.
 */
public class RatingConverter {

    public Rating fromDTO(RatingDTO ratingDTO, Recipe recipe, SocialMealsUser socialMealsUser) {
        return new Rating(ratingDTO.getStars(), recipe, socialMealsUser);
    }
}
