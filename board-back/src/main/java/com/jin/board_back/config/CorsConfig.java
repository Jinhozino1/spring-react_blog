// package com.jin.board_back.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
// import org.springframework.lang.NonNull;

// @Configuration
// public class CorsConfig implements WebMvcConfigurer{
    
//     @Override
//     public void addCorsMappings (@NonNull CorsRegistry corsRegistry) {
//         corsRegistry
//             // .addMapping("/**")
//             // .allowedMethods("*")
//             // .allowedOrigins("*");
//             .addMapping("/**") // 모든 엔드포인트에 대해 CORS 적용
//             .allowedOrigins("http://localhost:3000") // 특정 도메인만 허용 (React 개발 서버)
//             .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
//             .allowedHeaders("*") // 모든 헤더 허용
//             .allowCredentials(true) // 인증 정보 포함 (JWT, 쿠키 등)
//             .maxAge(3600); // 1시간 동안 CORS 캐시
//     }

//     @Bean
//     public WebMvcConfigurer corsConfigurer() {
//     return new WebMvcConfigurer() {
//         @Override
//         public void addCorsMappings(CorsRegistry registry) {
//             registry.addMapping("/**")
//                 .allowedOrigins("http://localhost:3000")
//                 .allowedMethods("*")
//                 .allowCredentials(true); // ★ 쿠키 허용
//         }
//     };
// }


// }
