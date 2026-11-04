package com.CodeCV.BackEnd.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()

                        .requestMatchers("/roles/**").hasRole("ADMIN")

                        .requestMatchers("/employees/create").hasRole("ADMIN")
                        .requestMatchers("/employees/all").hasRole("ADMIN")
                        .requestMatchers("/employees/getById/**").hasRole("ADMIN")
                        .requestMatchers("/employees/update").hasRole("ADMIN")
                        .requestMatchers("/employees/delete/**").hasRole("ADMIN")
                        .requestMatchers("/employees/myInfo").hasAnyRole("ADMIN", "EMPLOYEE")
                        .requestMatchers("/employees/employeesOnShift/**").hasAnyRole("ADMIN", "EMPLOYEE")
                        .requestMatchers("/employees/employeeShifts/**").hasAnyRole("ADMIN", "EMPLOYEE")
                        .requestMatchers("/employees/updatePassword").hasAnyRole("ADMIN", "EMPLOYEE")
                        .requestMatchers("/employees/addEmployeeToShift/**").hasAnyRole("ADMIN", "EMPLOYEE")

                        .requestMatchers("/shifts/create").hasRole("ADMIN")
                        .requestMatchers("/shifts/all").hasAnyRole("ADMIN", "EMPLOYEE")
                        .requestMatchers("/shifts/getById/**").hasRole("ADMIN")
                        .requestMatchers("/shifts/update/**").hasRole("ADMIN")
                        .requestMatchers("/shifts/delete/**").hasRole("ADMIN")
                        .requestMatchers("/shifts/missingEmployees").hasAnyRole("ADMIN", "EMPLOYEE")
                        .anyRequest().authenticated()
                        )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}