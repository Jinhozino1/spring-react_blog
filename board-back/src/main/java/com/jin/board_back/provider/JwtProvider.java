package com.jin.board_back.provider;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import io.jsonwebtoken.Claims;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

import com.jin.board_back.entity.UserEntity;
import com.jin.board_back.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private Key secretKey;


    private final UserRepository userRepository;

    @Value("${secret-key}")
    private String secret;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));  
    }

    public Key getSecretKey() {
        return secretKey;
    }

    public String create(String email) {

        Date expiredDate = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));
        
        String jwt = Jwts.builder()
            .setSubject(email)
            .setIssuedAt(new Date())
            .setExpiration(expiredDate)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
        return jwt;
    }

    // public String validate(String jwt) {
    //     Claims claims = null;

    //     try{
    //         claims = Jwts.parserBuilder()
    //             .setSigningKey(secretKey)
    //             .build()
    //             .parseClaimsJws(jwt)
    //             .getBody();
    //             System.out.println("✅ JwtProvider - Email extracted: " + claims.getSubject());
                
    //     } catch (Exception exception) {
    //         System.out.println("❌ JWT validation failed: " + exception.getMessage());
            
    //         exception.printStackTrace();
    //         return null;
    //     }
        
    //     return claims.getSubject();
    // }
    public String validate(String jwt) {
        Claims claims = null;
    
        try {
            claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
            String email = claims.getSubject();
            return email;
    
        } catch (Exception exception) {
            System.out.println("❌ JWT validation failed: " + exception.getMessage());
            exception.printStackTrace();
            return null;
        }
    }
}
