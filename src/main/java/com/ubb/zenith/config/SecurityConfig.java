package com.ubb.zenith.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;

    //private final JwtAuthenticationFilter jwtAuthFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Allow public access to the /LogIn endpoint
                        .requestMatchers("/api/swagger-ui/**", "/api/v3/api-docs/**", "/api/swagger-resources/**", "/api/webjars/**","/api/user/add").permitAll()
                        .requestMatchers("api/song/**")
                        .hasRole("USER")
                        .requestMatchers("api/playlist/**").hasRole("USER")
                        .requestMatchers("api/artist/**").hasRole("USER")
                        .requestMatchers("api/mood/**").hasRole("USER")
                        .requestMatchers("api/userPlaylist/**").hasRole("USER")
                        // Secure all other endpoints
                        .anyRequest().authenticated()
                )
               .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session
                       .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                //.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

       // Construiește configurația
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setUserDetailsService(userDetailsService);
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);  // Same strength used in AuthenticationProvider
    }


}
