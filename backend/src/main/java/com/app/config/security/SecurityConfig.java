package com.app.config.security;

import com.app.config.security.filter.JwtAuthenticationFilter;
import com.app.service.implementation.UserDetailServiceImpl;
import com.app.common.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtUtils jwtUtils;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(c -> {
                    CorsConfigurationSource source = request -> {
                        CorsConfiguration configuration = new CorsConfiguration();
                        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
                        configuration.setAllowedMethods(List.of("GET", "POST", "PUT"));
                        configuration.setAllowedHeaders(List.of("*"));
                        configuration.setAllowCredentials(true);
                        return configuration;
                    };
                    c.configurationSource(source);
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {

                    // PUBLIC Endpoints
                    http.requestMatchers(HttpMethod.POST, "/api/v1/auth/register").permitAll();
                    http.requestMatchers(HttpMethod.PUT, "/api/v1/auth/confirm-user").permitAll();
                    http.requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll();
                    http.requestMatchers(HttpMethod.PUT, "/api/v1/auth/forgot-password").permitAll();
                    http.requestMatchers(HttpMethod.PUT, "/api/v1/auth/reset-password").permitAll();
                    /* Swagger */
                    http.requestMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/swagger-ui.html").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll();

                    // PRIVATE Endpoints
                    http.requestMatchers(HttpMethod.GET, "/api/v1/appointments").authenticated();
                    http.requestMatchers(HttpMethod.POST, "/api/v1/appointments").authenticated();

                })
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtils), BasicAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailServiceImpl userDetailService
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
