package nl.miwgroningen.cohort5.socialmeals.model;

import org.springframework.data.repository.cdi.Eager;

import javax.persistence.*;
import java.util.Set;

/**
 * Britt van Mourik
 * Describes the details of a cookbook
 */

@Entity
public class Cookbook {

    @Id
    @GeneratedValue
    private Long cookbookId;

    private Long urlId;

    private String cookbookName;

    @ManyToOne
    private SocialMealsUser socialMealsUser;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "recipe_like", joinColumns = @JoinColumn(name = "cookbookId"),
            inverseJoinColumns = @JoinColumn(name = "recipeId"))
    Set<Recipe> recipeLikes;

    public Cookbook(String cookbookName, SocialMealsUser socialMealsUser, Set<Recipe> recipeLikes) {
        this.cookbookName = cookbookName;
        this.socialMealsUser = socialMealsUser;
        this.recipeLikes = recipeLikes;
    }

    public Cookbook() {
    }

    public Long getCookbookId() {
        return cookbookId;
    }

    public void setCookbookId(Long cookbookId) {
        this.cookbookId = cookbookId;
    }

    public String getCookbookName() {
        return cookbookName;
    }

    public void setCookbookName(String cookbookName) {
        this.cookbookName = cookbookName;
    }

    public SocialMealsUser getSocialMealsUser() {
        return socialMealsUser;
    }

    public void setSocialMealsUser(SocialMealsUser socialMealsUser) {
        this.socialMealsUser = socialMealsUser;
    }

    public Set<Recipe> getRecipeLikes() {
        return recipeLikes;
    }

    public void setRecipeLikes(Set<Recipe> recipeLikes) {
        this.recipeLikes = recipeLikes;
    }

    public Long getUrlId() {
        return urlId;
    }

    public void setUrlId(Long urlId) {
        this.urlId = urlId;
    }
}
