package nl.miwgroningen.cohort5.socialmeals.repository;

import nl.miwgroningen.cohort5.socialmeals.model.SocialMealsUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialMealsUserRepository extends JpaRepository<SocialMealsUser, Long> {

    Optional<SocialMealsUser> findByUsername(String username);

}
