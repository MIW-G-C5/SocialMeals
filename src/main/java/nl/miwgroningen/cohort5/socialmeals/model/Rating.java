package nl.miwgroningen.cohort5.socialmeals.model;

import javax.persistence.*;

/**
 * @author Wessel van Dommelen <w.r.van.dommelen@st.hanze.nl>
 *
 * describes the properties of a rating
 */
@Entity
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingId;

    private int stars;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recipe_id")
    Recipe recipe;

    @ManyToOne
    private SocialMealsUser socialMealsUser;

    public Rating(int stars, Recipe recipe, SocialMealsUser socialMealsUser) {
        this.stars = stars;
        this.recipe = recipe;
        this.socialMealsUser = socialMealsUser;
    }

    public Rating() {
    }

    public Long getRatingId() {
        return ratingId;
    }

    public void setRatingId(Long ratingId) {
        this.ratingId = ratingId;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public SocialMealsUser getSocialMealsUser() {
        return socialMealsUser;
    }

    public void setSocialMealsUser(SocialMealsUser socialMealsUser) {
        this.socialMealsUser = socialMealsUser;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int forks) {
        this.stars = forks;
    }
}
