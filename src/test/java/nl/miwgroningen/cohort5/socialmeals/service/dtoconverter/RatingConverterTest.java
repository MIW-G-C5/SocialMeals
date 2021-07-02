package nl.miwgroningen.cohort5.socialmeals.service.dtoconverter;

import nl.miwgroningen.cohort5.socialmeals.dto.RatingDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.SocialMealsUserDTO;
import nl.miwgroningen.cohort5.socialmeals.model.Rating;
import nl.miwgroningen.cohort5.socialmeals.model.Recipe;
import nl.miwgroningen.cohort5.socialmeals.model.SocialMealsUser;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for RatingConverter
 */

class RatingConverterTest {

    @Test
    void testDataConversionWithFromDTO() {
        //Arrange
        RatingConverter ratingConverter = new RatingConverter();

        RecipeDTO testRecipeDTO = new RecipeDTO();
        SocialMealsUserDTO testUserDTO = new SocialMealsUserDTO("testUser");
        RatingDTO testRatingDTO = new RatingDTO(4, testRecipeDTO, testUserDTO);

        Recipe testRecipe = new Recipe();
        SocialMealsUser testUser = new SocialMealsUser();

        //Act
        Rating testRating = ratingConverter.fromDTO(testRatingDTO, testRecipe, testUser);

        //Assert
        assertThat(testRating.getStars()).isEqualTo(testRatingDTO.getStars());
    }
}