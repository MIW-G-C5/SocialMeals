package nl.miwgroningen.cohort5.socialmeals.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Wessel van Dommelen <w.r.van.dommelen@st.hanze.nl>
 * describes the details of a SocialMealsUser
 */

@Entity
public class SocialMealsUser implements UserDetails {

    @Id
    @GeneratedValue
    private Long userId;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    @OneToMany(mappedBy = "socialMealsUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Recipe> userRecipes;

    @OneToMany(mappedBy = "socialMealsUser", cascade = CascadeType.ALL)
    private Set<Cookbook> userCookbooks;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));

        return authorityList;
    }

    public Set<Cookbook> getUserCookbooks() {
        return userCookbooks;
    }

    public void setUserCookbooks(Set<Cookbook> userCookbooks) {
        this.userCookbooks = userCookbooks;
    }

    public Set<Recipe> getUserRecipes() {
        return userRecipes;
    }

    public void setUserRecipes(Set<Recipe> userRecipes) {
        this.userRecipes = userRecipes;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
