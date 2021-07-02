package nl.miwgroningen.cohort5.socialmeals.service.implementation;

import nl.miwgroningen.cohort5.socialmeals.dto.SocialMealsUserDTO;
import nl.miwgroningen.cohort5.socialmeals.model.SocialMealsUser;
import nl.miwgroningen.cohort5.socialmeals.repository.SocialMealsUserRepository;
import nl.miwgroningen.cohort5.socialmeals.service.dtoconverter.SocialMealsUserConverter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Wessel van Dommelen <w.r.van.dommelen@st.hanze.nl>
 *
 * Collects and stores Userdetails in the MySQL Database
 */

@Service
public class SocialMealsUserDetailService implements UserDetailsService {

    private final SocialMealsUserRepository socialMealsUserRepository;
    private final SocialMealsUserConverter socialMealsUserConverter;

    public SocialMealsUserDetailService(SocialMealsUserRepository socialMealsUserRepository) {
        this.socialMealsUserRepository = socialMealsUserRepository;
        socialMealsUserConverter = new SocialMealsUserConverter();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return socialMealsUserRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User " + username + " not found"));
    }

    public List<SocialMealsUserDTO> getAll() {
        List<SocialMealsUser> socialMealsUsers = socialMealsUserRepository.findAll();
        return socialMealsUserConverter.toListSocialMealsUserDTOs(socialMealsUsers);
    }

    public SocialMealsUserDTO getUserByUsername(String username) {
        Optional<SocialMealsUser> socialMealsUser = socialMealsUserRepository.findByUsername(username);
        if (socialMealsUser.isEmpty()) {
            return null;
        }
        return socialMealsUserConverter.toDTO(socialMealsUser.get());
    }

    public void addSocialMealsUser(String username, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);

        SocialMealsUser user = new SocialMealsUser();
        user.setUsername(username);
        user.setPassword(encodedPassword);

        socialMealsUserRepository.save(user);
    }

    public SocialMealsUser getUserByDTO(SocialMealsUserDTO socialMealsUserDTO) {
        Optional<SocialMealsUser> socialMealsUser =
                socialMealsUserRepository.findByUsername(socialMealsUserDTO.getUsername());
        if (socialMealsUser.isEmpty()) {
            return null;
        }
        return socialMealsUser.get();
    }


}
