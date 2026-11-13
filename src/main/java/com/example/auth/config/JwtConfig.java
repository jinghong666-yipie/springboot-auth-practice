package com.example.auth.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfig {
    @Bean
    public SecretKey jwtSigningKey(JwtProperties properties) {
        if (properties.getSecretBase64() == null || properties.getSecretBase64().isBlank()) {
            throw new IllegalStateException("请设置环境变量 JWT_SECRET_BASE64");
        }
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(properties.getSecretBase64()));
    }
}
