package nl.miwgroningen.cohort5.socialmeals.service.dtoconverter;

import nl.miwgroningen.cohort5.socialmeals.dto.IngredientDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.IngredientRecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.model.IngredientRecipe;
import nl.miwgroningen.cohort5.socialmeals.model.Recipe;
import nl.miwgroningen.cohort5.socialmeals.service.mocks.MockIngredientService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class IngredientRecipeConverterTest {

    @Test
    void testDataConversionWithFromDTO() {
        //Arrange
        MockIngredientService mockIngredientService = new MockIngredientService();
        IngredientRecipeConverter ingredientRecipeConverter = new IngredientRecipeConverter(mockIngredientService);

        IngredientDTO ingredientDTO = new IngredientDTO("testIngredient");
        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setRecipeName("testRecipeDTO");
        IngredientRecipeDTO testIngredientRecipeDTO =
                new IngredientRecipeDTO(ingredientDTO, recipeDTO, 5.0, "units");

        Recipe recipe = new Recipe();
        recipe.setRecipeName("testRecipe");

        //Act
        IngredientRecipe testIngredientRecipe = ingredientRecipeConverter.fromDTO(testIngredientRecipeDTO, recipe);

        //Assert
        assertThat(testIngredientRecipe.getQuantity()).isEqualTo(testIngredientRecipeDTO.getQuantity());
        assertThat(testIngredientRecipe.getQuantityType()).isEqualTo(testIngredientRecipeDTO.getQuantityType());
        assertThat(testIngredientRecipe.getIngredient().getIngredientName())
                .isEqualTo(testIngredientRecipeDTO.getIngredientDTO().getIngredientName());
    }
}