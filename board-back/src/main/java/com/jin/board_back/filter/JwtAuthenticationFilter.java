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
import com.jin.board_back.service.CustomUserDetailsService;

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
    private final CustomUserDetailsService userDetailsService;

    private static final List<String> permitAllPaths = List.of(  
        "/file/",
        "/api/v1/auth/",
        "/api/v1/search/",
        "/api/v1/user",        // GETㅂ
        "/api/v1/user/",
        "/api/v1/search/",
        "/api/v1/user-board-list/"
    );

    // private boolean isPermitAllPath(HttpServletRequest request) {
    //     String path = request.getRequestURI();
    //     String method = request.getMethod();

    //     // /api/v1/user 와 정확히 같을 때만 허용
    //     if (path.equals("/api/v1/user") && method.equals("GET")) return true;

    //     // /file/ 등 prefix 허용
    //     return permitAllPaths.stream().anyMatch(p -> 
    //         p.endsWith("/") && path.startsWith(p)
    //     );
    // }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, 
            @NonNull HttpServletResponse response, 
            @NonNull FilterChain filterChain) 
            throws ServletException, IOException {

        try {
            String token = parseBearerToken(request);
            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            String email = jwtProvider.validate(token);
            if (email == null) {
                filterChain.doFilter(request, response);
                return;
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            AbstractAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            // System.out.println("authenticationToken =  " + authenticationToken);
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authenticationToken);
            
            SecurityContextHolder.setContext(securityContext);
                
            // SecurityContextHolder.getContext().setAuthentication(authenticationToken);


        } catch (Exception exception) {
            exception.printStackTrace();
            return ;
        }

        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request) {
    // 1. 쿠키에서 accessToken 확인
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("accessToken")) {
                String token = cookie.getValue();
                return token;
            }
        }
    }
    
    return null;
    }

    // private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
    //     response.setContentType("application/json");
    //     response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    //     response.getWriter().write("{\"code\": \"401\", \"message\": \"" + message + "\"}");
    // }
}
