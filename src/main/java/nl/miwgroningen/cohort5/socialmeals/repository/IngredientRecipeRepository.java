package nl.miwgroningen.cohort5.socialmeals.repository;

import nl.miwgroningen.cohort5.socialmeals.model.IngredientRecipe;
import nl.miwgroningen.cohort5.socialmeals.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientRecipeRepository extends JpaRepository<IngredientRecipe, Long> {
    List<IngredientRecipe> findIngredientRecipeByRecipe(Recipe recipe);
}
