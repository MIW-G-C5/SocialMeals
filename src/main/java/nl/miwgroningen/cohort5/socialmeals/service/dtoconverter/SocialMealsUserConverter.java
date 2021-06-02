package nl.miwgroningen.cohort5.socialmeals.service.dtoconverter;

import nl.miwgroningen.cohort5.socialmeals.dto.SocialMealsUserDTO;
import nl.miwgroningen.cohort5.socialmeals.model.SocialMealsUser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wessel van Dommelen <w.r.van.dommelen@st.hanze.nl>
 */
public class SocialMealsUserConverter {

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
}
