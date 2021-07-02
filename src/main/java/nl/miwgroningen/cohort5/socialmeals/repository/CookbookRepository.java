package nl.miwgroningen.cohort5.socialmeals.repository;

import nl.miwgroningen.cohort5.socialmeals.model.Cookbook;
import nl.miwgroningen.cohort5.socialmeals.model.SocialMealsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CookbookRepository extends JpaRepository<Cookbook, Long> {

    Optional<Cookbook> findByUrlId(Long urlId);

    List<Cookbook> findCookbooksBySocialMealsUser(SocialMealsUser socialMealsUser);

    @Query("SELECT MAX(urlId) FROM Cookbook")
    Long getMaxUrlId();

}
