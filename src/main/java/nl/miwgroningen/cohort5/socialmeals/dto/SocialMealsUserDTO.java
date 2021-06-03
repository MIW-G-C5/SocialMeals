package nl.miwgroningen.cohort5.socialmeals.dto;

/**
 * @author Wessel van Dommelen <w.r.van.dommelen@st.hanze.nl>
 */
public class SocialMealsUserDTO {

    private String username;

    public SocialMealsUserDTO(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
