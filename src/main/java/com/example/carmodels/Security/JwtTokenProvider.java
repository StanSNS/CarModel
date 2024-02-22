package com.example.carmodels.Security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    /**
     * Generates a JWT token for the provided authentication object.
     *
     * @param authentication The user's authentication.
     * @return A JWT token as a string.
     */
    public String generateToken(Authentication authentication) {
        String email = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key())
                .compact();
    }

    /**
     * Creates a Key object from the JWT secret.
     *
     * @return A Key object created from the JWT secret.
     */
    private Key key() {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret));
    }

    /**
     * Extracts the email from a JWT token.
     *
     * @param token The JWT token as a string.
     * @return The username extracted from the token.
     */
    public String getEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    /**
     * Validates a JWT token by checking its signature and expiration.
     *
     * @param token The JWT token as a string.
     * @return true if the token is valid, false if it's expired or has an invalid signature.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(token);
            return true;
        } catch (ExpiredJwtException e) {
            return false;
        }
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public long getJwtExpirationDate() {
        return jwtExpirationDate;
    }

    public void setJwtExpirationDate(long jwtExpirationDate) {
        this.jwtExpirationDate = jwtExpirationDate;
    }
}