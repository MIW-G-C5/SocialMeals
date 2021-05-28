package nl.miwgroningen.cohort5.socialmeals.repository;

import nl.miwgroningen.cohort5.socialmeals.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

}
