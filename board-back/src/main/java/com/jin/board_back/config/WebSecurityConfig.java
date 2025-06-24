package com.jin.board_back.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.jin.board_back.filter.JwtAuthenticationFilter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.AuthenticationException;

import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    // private final CustomUserDetailsService customUserDetailsService;


    @Bean
    public SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ CORS 설정 적용
            .csrf(csrf -> csrf.disable())
            .httpBasic(httpBasic -> httpBasic.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/", "/favicon.ico", "/api/v1/auth/**", "/api/v1/search/**", "/file/**", "/api/v1/search-list/**", "/api/v1/user-board-list/**" ).permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/board/**").permitAll()
                .anyRequest().authenticated()
            )
            .anonymous(anonymous -> anonymous.disable())
            .exceptionHandling(ex -> ex.authenticationEntryPoint(new FailedAuthenticationEntryPoint()))
            .requiresChannel(channel -> channel
            .anyRequest().requiresSecure()
            );
            // .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
            "http://localhost:3000",
            "http://43.200.5.16:3000", 
            "https://jinhozinoboard.click",
            "https://www.jinhozinoboard.click"
        )); // 프론트엔드 도메인 허용
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")); // HTTP 메서드 허용
        configuration.setAllowedHeaders(List.of("Content-Type", "Authorization")); // 필요한 헤더 허용
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

@Component
class FailedAuthenticationEntryPoint implements AuthenticationEntryPoint {
    
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, 
                        AuthenticationException authException
    ) throws IOException, ServletException{

        String authorization = request.getHeader("Authorization");
        String token = null;

        if (authorization != null && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
        }
        
        System.out.println("🚨 401 Unauthorized 발생! URL: " + request.getRequestURI());
        System.out.println("Authorization Header: " + request.getHeader("Authorization"));
        System.out.println("Extracted Token: " + token);
        
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("WWW-Authenticate", "Bearer realm=\"Access to protected resources\"");
        response.getWriter().write("{\"code\":\"401\", \"message\": \"Unauthorized - Invalid or missing token\"}");
    }
}
