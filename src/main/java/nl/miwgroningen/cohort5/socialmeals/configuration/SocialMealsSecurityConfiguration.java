package nl.miwgroningen.cohort5.socialmeals.configuration;

import nl.miwgroningen.cohort5.socialmeals.service.implementation.SocialMealsUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Wessel van Dommelen <w.r.van.dommelen@st.hanze.nl>
 *
 *     Determines who can do what
 */

@Configuration
@EnableWebSecurity
public class SocialMealsSecurityConfiguration extends WebSecurityConfigurerAdapter {

    SocialMealsUserDetailService socialMealsUserDetailsService;

    public SocialMealsSecurityConfiguration(SocialMealsUserDetailService socialMealsUserDetailsService) {
        this.socialMealsUserDetailsService = socialMealsUserDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/css/**", "/js/**").permitAll()
                .antMatchers("/", "/recipes", "/recipes/*").permitAll()
                .antMatchers("/user/new", "/MyKitchen").permitAll()
                .antMatchers("/Cookbook/*", "/recipe/new", "/recipe/update/*").authenticated()
                .anyRequest().authenticated().and()
                .formLogin().and()
                .logout().logoutSuccessUrl("/recipes");
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(socialMealsUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
