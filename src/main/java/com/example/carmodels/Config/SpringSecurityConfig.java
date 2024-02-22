package com.example.carmodels.Config;

import com.example.carmodels.Security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.example.carmodels.Constants.JWTConst.JWT_COOKIE_NAME;
import static com.example.carmodels.Constants.SessionConst.JS_SESSION;

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
     * @return The configured SecurityFilterChain bean.
     * @throws Exception If an error occurs while configuring security.
     */
    @Bean
    // Define a SecurityFilterChain bean for configuring security filters in Spring Security.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Disable CSRF protection
        http.csrf(AbstractHttpConfigurer::disable);

        // Configure authorization rules
        http.authorizeHttpRequests((authorize) -> {
                    // Permit access to specified endpoints
                    authorize.requestMatchers(
                            "/auth/**",
                            "/error",
                            "/**"
                    ).permitAll();
                    // Require authentication for any other requests
                    authorize.anyRequest().authenticated();
                })
                // Configure logout behavior
                .logout((logout -> {
                    logout.logoutUrl("/auth/logout");
                    logout.deleteCookies(JS_SESSION, JWT_COOKIE_NAME);
                    logout.invalidateHttpSession(true);
                    logout.clearAuthentication(true);
                    logout.logoutSuccessUrl("/auth/login");
                }));

        // Configure exception handling
        http.exceptionHandling((ex) -> {
            // Handle access denied exceptions by setting response status to 404
            ex.accessDeniedHandler((request, response, accessDeniedException) -> response.setStatus(404));
            // Set authentication entry point for authentication exceptions
            ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.NOT_FOUND));
            // Redirect to error page for access denied exceptions
            ex.accessDeniedPage("/error");
        });

        // Configure OAuth2 login with default settings
        http.oauth2Login(Customizer.withDefaults());

        // Configure HTTP Basic authentication with default settings
        http.httpBasic(Customizer.withDefaults());

        // Add custom JWT authentication filter before the UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Build and return the configured SecurityFilterChain
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