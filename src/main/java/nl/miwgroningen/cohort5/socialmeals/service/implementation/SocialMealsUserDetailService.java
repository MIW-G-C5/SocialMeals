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

/**
 * @author Wessel van Dommelen <w.r.van.dommelen@st.hanze.nl>
 */

@Service
public class SocialMealsUserDetailService implements UserDetailsService {

    SocialMealsUserConverter socialMealsUserConverter = new SocialMealsUserConverter();

    private SocialMealsUserRepository socialMealsUserRepository;

    public SocialMealsUserDetailService(SocialMealsUserRepository socialMealsUserRepository) {
        this.socialMealsUserRepository = socialMealsUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return socialMealsUserRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User " + username + " not found")) ;
    }

    public List<SocialMealsUserDTO> getAll() {
        List<SocialMealsUser> socialMealsUsers = socialMealsUserRepository.findAll();
        return socialMealsUserConverter.toListSocialMealsUserDTOs(socialMealsUsers);
    }

    public void addSocialMealsUser(String username, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);

        SocialMealsUser user = new SocialMealsUser();
        user.setUsername(username);
        user.setPassword(encodedPassword);

        socialMealsUserRepository.save(user);
    }




}
