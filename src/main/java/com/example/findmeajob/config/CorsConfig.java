//package com.example.findmeajob.config;
//
//import org.jetbrains.annotations.NotNull;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class CorsConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(@NotNull CorsRegistry registry) {
//        registry.addMapping("/**") // Allow CORS for all endpoints
//                .allowedOrigins("https://job-matcher-frontend.vercel.app","http://localhost:3000") // Replace with your frontend URL
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods
//                .allowedHeaders("*") // Allow all headers
//                .allowCredentials(true); // Allow cookies/auth headers if needed
//    }
//}