package com.example.GameDepotAPI.config;

import com.example.GameDepotAPI.exception.JwtAccessDeniedHandler;
import com.example.GameDepotAPI.exception.JwtAuthenticationEntryPoint;
import com.example.GameDepotAPI.service.jwt.JwtAuthenticationFilter;
import io.jsonwebtoken.security.MacAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.Base64;

@Configuration
public class SecurityConfig{
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    private final JwtAuthenticationFilter jwtFilter;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter, JwtAuthenticationEntryPoint authenticationEntryPoint, JwtAccessDeniedHandler accessDeniedHandler) {
        this.jwtFilter = jwtFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable()).sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                .exceptionHandling(exceptions ->
                        exceptions
                                .authenticationEntryPoint(
                                        authenticationEntryPoint
                                )
                                .accessDeniedHandler(
                                        accessDeniedHandler
                                )
                )
                .authorizeHttpRequests(auth -> auth

                        // Registro
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/usuario"
                        ).permitAll()

                        // Inicio de sesión
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/auth"
                        ).permitAll()

                        // Catálogo público
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/videojuego/**"
                        ).permitAll()

                        // Futuras rutas administrativas
                        .requestMatchers(
                                "/api/admin/**"
                        ).hasRole("ADMIN")

                        //Carrito
                        .requestMatchers(
                                "/api/carrito/**",
                                "/api/carrito/items/**"
                        ).hasRole("CLIENTE")

                        //Ordenes
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/ordenes"
                        ).hasRole("CLIENTE")

                        .requestMatchers(
                                "/api/ordenes/**"
                        ).hasRole("CLIENTE")

                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.GET,
                                "/uploads/videojuegos/**"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/pagos/mercado-pago/webhook"
                        ).permitAll()

                        // Todo lo demás requiere JWT
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
