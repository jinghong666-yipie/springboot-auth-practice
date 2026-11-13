package com.example.auth.util;

import com.example.auth.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private final SecretKey signingKey;
    private final JwtProperties properties;

    public JwtUtil(SecretKey signingKey, JwtProperties properties) {
        this.signingKey = signingKey;
        this.properties = properties;
    }

    public String generate(Long userId, String username) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(userId.toString())
                .claim("username", username)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(properties.getExpirationMs())))
                .signWith(signingKey)
                .compact();
    }

    public Claims parseAndValidate(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
