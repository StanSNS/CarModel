package com.example.carmodels.config;

import com.example.carmodels.Security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.example.carmodels.constants.RoleConst.BASIC_USER;


@Configuration
@EnableMethodSecurity
public class SpringSecurityConfig implements WebMvcConfigurer {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SpringSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    /**
     * Creates a BCryptPasswordEncoder instance for password hashing and validation.
     *
     * @return A BCryptPasswordEncoder instance for password encoding.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the security filter chain for handling HTTP security configurations.
     * This bean defines various security rules and permissions for different endpoints.
     *
     * @param http The HttpSecurity object to configure security settings.
     * @return     The configured SecurityFilterChain bean.
     * @throws Exception If an error occurs while configuring security.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Disable CSRF protection
        http.csrf(AbstractHttpConfigurer::disable);

        // Define authorization rules for different request matchers
        http.authorizeHttpRequests((authorize) -> {
            // Define request matchers and permissions
            authorize.requestMatchers(
                    "/auth/**"
            ).permitAll();
            authorize.requestMatchers("/").authenticated();
            authorize.requestMatchers("/").hasRole(BASIC_USER);
            authorize.requestMatchers("/add").authenticated();
            authorize.requestMatchers("/add").hasRole(BASIC_USER);
            authorize.requestMatchers("/addmodel").authenticated();
            authorize.requestMatchers("/addmodel").hasRole(BASIC_USER);
            authorize.requestMatchers("/edit/{id}").authenticated();
            authorize.requestMatchers("/edit/{id}").hasRole(BASIC_USER);
            authorize.requestMatchers("/updatemodel").authenticated();
            authorize.requestMatchers("/updatemodel").hasRole(BASIC_USER);
            authorize.requestMatchers("/delete/{id}").authenticated();
            authorize.requestMatchers("/delete/{id}").hasRole(BASIC_USER);
            authorize.anyRequest().authenticated();
        });
        // Enable basic authentication with default settings
        http.httpBasic(Customizer.withDefaults());

        // Add JWT authentication filter before the UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Creates an AuthenticationManager using the provided AuthenticationConfiguration.
     *
     * @param configuration The AuthenticationConfiguration object to configure the manager.
     * @return An AuthenticationManager instance.
     * @throws Exception if there's an issue creating the AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}