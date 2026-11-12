package com.agrios.user.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // Generate signing key
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    // Generate JWT token
    public String generateToken(String email) {

        Date currentDate = new Date();

        Date expiryDate = new Date(
                currentDate.getTime() + expiration
        );

        return Jwts.builder()
                .subject(email)
                .issuedAt(currentDate)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    // Extract email from JWT
    public String extractUsername(String token) {

        return extractAllClaims(token).getSubject();
    }

    // Validate JWT token
    public boolean isTokenValid(String token, String email) {

        try {
            Claims claims = extractAllClaims(token);

            String username = claims.getSubject();

            Date expirationDate = claims.getExpiration();

            return username.equals(email)
                    && expirationDate.after(new Date());

        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    // Extract all claims from JWT
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}