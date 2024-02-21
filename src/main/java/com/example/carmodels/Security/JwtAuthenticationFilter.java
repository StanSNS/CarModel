package com.example.carmodels.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.example.carmodels.constants.JWTConst.BEGIN_INDEX_TO_RAW_TOKEN;
import static com.example.carmodels.constants.JWTConst.COOKIE_NAME;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }


    /**
     * Performs filtering of incoming HTTP requests to authenticate users using JWT tokens.
     * If a valid JWT token is present in the request, it authenticates the user and sets the
     * authentication details in the SecurityContextHolder.
     *
     * @param request     The HTTP servlet request.
     * @param response    The HTTP servlet response.
     * @param filterChain The filter chain to proceed with after authentication.
     * @throws ServletException If an error occurs during the servlet operation.
     * @throws IOException      If an I/O error occurs during the servlet operation.
     */
    @Override
    public void doFilterInternal(@NonNull HttpServletRequest request,
                                 @NonNull HttpServletResponse response,
                                 @NonNull FilterChain filterChain) throws ServletException, IOException {
        // Retrieve token from the request
        String token = getTokenFromRequest(request);

        // Validate the token and authenticate the user if token is valid
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            // Extract email from the token
            String email = jwtTokenProvider.getEmail(token);
            // Load user details using the email
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            // Create authentication token
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            // Set authentication details
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // Set authentication in SecurityContextHolder
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // Proceed with the filter chain
        filterChain.doFilter(request, response);
    }

    /**
     * Retrieves the JWT token from the HTTP servlet request's cookies.
     *
     * @param request The HTTP servlet request.
     * @return        The JWT token extracted from the request, or null if not found.
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        if (request.getCookies() != null && request.getCookies().length > 0) {
            // Filter cookies to find the one with the specified name
            List<Cookie> filteredCookies = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(COOKIE_NAME))
                    .toList();

            // Extract token value from the filtered cookies
            String rawToken;
            if (!filteredCookies.isEmpty()) {
                rawToken = filteredCookies.get(0).getValue();

                if (rawToken != null) {
                    // Return the token value after removing unnecessary prefix
                    return rawToken.substring(BEGIN_INDEX_TO_RAW_TOKEN);
                }
            }
        }
        return null;
    }

}