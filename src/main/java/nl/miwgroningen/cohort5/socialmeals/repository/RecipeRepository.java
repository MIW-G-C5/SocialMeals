package nl.miwgroningen.cohort5.socialmeals.repository;


import nl.miwgroningen.cohort5.socialmeals.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
