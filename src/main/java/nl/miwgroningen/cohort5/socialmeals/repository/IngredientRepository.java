package nl.miwgroningen.cohort5.socialmeals.repository;

import nl.miwgroningen.cohort5.socialmeals.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    Optional<Ingredient> findByIngredientName(String ingredientName);

    @Query("SELECT ingredientName FROM Ingredient where ingredientName like %:keyword% ORDER BY ingredientName")
    List<String> search(@Param("keyword") String keyword);
}
