package com.jin.board_back.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.StringUtils;

import com.jin.board_back.provider.JwtProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    private static final List<String> permitAllPaths = List.of(
        // "/",
    "/file/",
    "/api/v1/auth/",
    "/api/v1/search/"
);

    private boolean isPermitAllPath(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        // 특정 경로 + GET 메서드만 허용
        if (path.startsWith("/api/v1/board/") && method.equals("GET")) return true;

        return permitAllPaths.stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, 
            @NonNull HttpServletResponse response, 
            @NonNull FilterChain filterChain) 
            throws ServletException, IOException {

                
        try {
            if (isPermitAllPath(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = parseBearerToken(request);
            System.out.println("✅ Token: " + token);
            if (token == null) {
                setUnauthorizedResponse(response, "Token is missing");
                System.out.println("Token is missing");
                return; // ✅ 바로 종료
            }

            String email = jwtProvider.validate(token);
            System.out.println("✅ Validated Email from Token: " + email);
            if (email == null) {
                sendUnauthorizedResponse(response, "Invalid or expired token.");
                filterChain.doFilter(request, response);
                return;
            }

            // AbstractAuthenticationToken authenticationToken = 
            //     new UsernamePasswordAuthenticationToken(email, null, AuthorityUtils.NO_AUTHORITIES);
            
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            System.out.println("✅ Loaded UserDetails: " + userDetails.getUsername());
            
            AbstractAuthenticationToken authenticationToken = 
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(securityContext);

            System.out.println("✅ Authentication set in SecurityContextHolder: " + authenticationToken);
            
        } catch (Exception exception) {
            sendUnauthorizedResponse(response, "Authentication error.");
            exception.printStackTrace();
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request) {
    // 1. Authorization 헤더에서 시도
    String authorization = request.getHeader("Authorization");
    if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
        String token = authorization.substring(7);
        return token;
    }

    // 2. 쿠키에서 accessToken 확인
    if (request.getCookies() != null) {
        for (Cookie cookie : request.getCookies()) {
            if ("accessToken".equals(cookie.getName())) {
                String token = cookie.getValue();
                return token;
            }
        }
    }

    return null;
    }


    // private String parseBearerToken(HttpServletRequest request) {
    //     String authorization = request.getHeader("Authorization");

    //     boolean hasAuthorization = StringUtils.hasText(authorization);
    //     if (!hasAuthorization) return null;

    //     boolean isBearer = authorization.startsWith("Bearer ");
    //     if (!isBearer) return null;

    //     String token = authorization.substring(7);
    //     return token;
    // }


    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // response.getWriter().write("{\"code\": \"401\", \"message\": \"" + message + "\"}");
    }

    private void setUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("WWW-Authenticate", "Bearer realm=\"access to protected resources\"");
        // response.getWriter().write("{\"code\":\"401\", \"message\": \"" + message + "\"}");
    }
}
