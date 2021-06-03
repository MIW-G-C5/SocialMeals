package nl.miwgroningen.cohort5.socialmeals.service.dtoconverter;

import nl.miwgroningen.cohort5.socialmeals.dto.SocialMealsUserDTO;
import nl.miwgroningen.cohort5.socialmeals.model.SocialMealsUser;
import nl.miwgroningen.cohort5.socialmeals.repository.SocialMealsUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Wessel van Dommelen <w.r.van.dommelen@st.hanze.nl>
 */
public class SocialMealsUserConverter {

    private SocialMealsUserRepository socialMealsUserRepository;

    public SocialMealsUserConverter(SocialMealsUserRepository socialMealsUserRepository) {
        this.socialMealsUserRepository = socialMealsUserRepository;
    }

    public List<SocialMealsUserDTO> toListSocialMealsUserDTOs(List<SocialMealsUser> socialMealsUsers) {
        List<SocialMealsUserDTO> resultList = new ArrayList<>();

        for (SocialMealsUser socialMealsUser : socialMealsUsers) {
            resultList.add(toDTO(socialMealsUser));
        }

        return resultList;
    }

    public SocialMealsUserDTO toDTO(SocialMealsUser socialMealsUser) {
        return new SocialMealsUserDTO(socialMealsUser.getUsername());
    }

    public SocialMealsUser fromDTO(SocialMealsUserDTO socialMealsUserDTO) {
        Optional<SocialMealsUser> socialMealsUser =
                socialMealsUserRepository.findByUsername(socialMealsUserDTO.getUserName());
        if (socialMealsUser.isEmpty()) {
            return null;
        }
        return socialMealsUser.get();
    }
}
