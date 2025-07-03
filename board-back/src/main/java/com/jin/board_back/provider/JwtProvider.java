package com.jin.board_back.provider;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SignatureException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private Key secretKey;

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
        Instant now = Instant.now();
        Date issuedAt = Date.from(now);
        Date expiredDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
        
        String jwt = Jwts.builder()
            .setSubject(email)
            .setIssuedAt(issuedAt)
            .setExpiration(expiredDate)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
        return jwt;
    }

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
    
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            System.out.println("❌ JWT 만료됨: " + e.getMessage());
        } catch (io.jsonwebtoken.SignatureException e) {
            System.out.println("❌ JWT 서명 오류: " + e.getMessage());
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            System.out.println("❌ JWT 형식 오류: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ 기타 JWT 오류: " + e.getMessage());
            e.printStackTrace();
        }
    return null;
    }
}
