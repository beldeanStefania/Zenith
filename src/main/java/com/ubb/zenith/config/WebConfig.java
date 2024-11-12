//package com.ubb.zenith.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**") // Permite CORS pentru toate rutele
//                .allowedOrigins("http://localhost:3000") // Permite cereri doar de la frontend-ul React
//                .allowedMethods("GET", "POST", "PUT", "DELETE") // Permite metodele HTTP
//                .allowedHeaders("*") // Permite toate header-ele
//                .allowCredentials(true); // Permite trimiterea de cookie-uri și credențiale
//    }
//}
//
//
