package nl.miwgroningen.cohort5.socialmeals.service.dtoconverter;
import nl.miwgroningen.cohort5.socialmeals.dto.RecipeDTO;
import nl.miwgroningen.cohort5.socialmeals.dto.SocialMealsUserDTO;
import nl.miwgroningen.cohort5.socialmeals.model.Recipe;
import nl.miwgroningen.cohort5.socialmeals.model.SocialMealsUser;
import nl.miwgroningen.cohort5.socialmeals.repository.SocialMealsUserRepository;
import nl.miwgroningen.cohort5.socialmeals.service.implementation.SocialMealsUserDetailService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SocialMealsUserConverterTest {

    @Test
    void toListSocialMealsUserDTOs(){

        //arrange
        SocialMealsUserConverter testConverter = new SocialMealsUserConverter();

        SocialMealsUser userOne = new SocialMealsUser();
        SocialMealsUser userTwo = new SocialMealsUser();
        SocialMealsUser userThree = new SocialMealsUser();

        userOne.setUsername("Kwik");
        userTwo.setUsername("Kwek");
        userThree.setUsername("Kwak");


        List<SocialMealsUser> testUserList = new ArrayList<>();
        testUserList.add(userOne);
        testUserList.add(userTwo);
        testUserList.add(userThree);

        //act
        List<SocialMealsUserDTO> testDTOList = testConverter.toListSocialMealsUserDTOs(testUserList);

        //assert
        for (int user = 0; user < testDTOList.size(); user++) {
            assertThat(testDTOList.get(user).getUsername()).isEqualTo(testUserList.get(user).getUsername());
        }
    }

    @Test
    void toDTO() {

        //arrange
        SocialMealsUserConverter testConverter = new SocialMealsUserConverter();
        SocialMealsUser testSocialMealsUser = new SocialMealsUser();
        testSocialMealsUser.setUsername("Joost");

        //act
        SocialMealsUserDTO testDTO = testConverter.toDTO(testSocialMealsUser);

        //assert
        assertThat(testDTO.getUsername()).isEqualTo("Joost");

    }

}
