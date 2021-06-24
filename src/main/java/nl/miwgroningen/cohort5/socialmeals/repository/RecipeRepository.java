package nl.miwgroningen.cohort5.socialmeals.repository;

import nl.miwgroningen.cohort5.socialmeals.model.Recipe;
import nl.miwgroningen.cohort5.socialmeals.model.SocialMealsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Optional<Recipe> findByRecipeName(String recipeName);

    Optional<Recipe> findByUrlId(Long urlId);

    List<Recipe> findRecipesBySocialMealsUser(SocialMealsUser socialMealsUser);

    @Query("SELECT recipeName FROM Recipe where recipeName like %:keyword%")
    List<String> search(@Param("keyword") String keyword);

    @Query("SELECT MAX(urlId) FROM Recipe")
    Long getMaxUrlId();
}
