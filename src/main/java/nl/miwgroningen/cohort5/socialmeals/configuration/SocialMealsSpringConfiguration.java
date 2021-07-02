package nl.miwgroningen.cohort5.socialmeals.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

/**
 * @author A.H. van Zessen
 *
 * Controls the configuration of Spring
 */

@Configuration
public class SocialMealsSpringConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/static/**").addResourceLocations("/resources/static")
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS)
                .noTransform()
                .mustRevalidate());
    }
}
