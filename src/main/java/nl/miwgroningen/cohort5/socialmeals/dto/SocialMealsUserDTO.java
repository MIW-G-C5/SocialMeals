package nl.miwgroningen.cohort5.socialmeals.dto;

/**
 * @author Wessel van Dommelen <w.r.van.dommelen@st.hanze.nl>
 */
public class SocialMealsUserDTO {

    private String username;

    public SocialMealsUserDTO(String username) {
        this.username = username;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }
}
