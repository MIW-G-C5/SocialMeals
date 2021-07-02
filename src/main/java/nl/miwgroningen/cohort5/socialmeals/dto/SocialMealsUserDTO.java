package nl.miwgroningen.cohort5.socialmeals.dto;


/**
 * @author Wessel van Dommelen <w.r.van.dommelen@st.hanze.nl>
 *
 * Describes the details of a SocialMealsUserDTO
 */
public class SocialMealsUserDTO {

    private String username;

    public SocialMealsUserDTO(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SocialMealsUserDTO that = (SocialMealsUserDTO) o;
        return username.equals(that.username);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
