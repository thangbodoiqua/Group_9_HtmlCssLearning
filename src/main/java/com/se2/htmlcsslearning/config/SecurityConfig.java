package com.se2.htmlcsslearning.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // guest
                        .requestMatchers(
                                "/",
                                "/auth/**",
                                "/css/**", "/js/**", "/images/**",

                                // Guest dùng được
                                "/lessons",
                                "/lessons/view/**",
                                "/examples/**",
                                "/code/compile"
                        ).permitAll()

                        //  learner
                        .requestMatchers(
                                "/home",
                                "/profile/**",
                                "/notes/**",
                                "/progress/**",

                                "/practice/**",
                                "/compare/**",

                                "/challenges/**"
                        ).hasAuthority("USER")

                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/auth/signin")
                        .loginProcessingUrl("/auth/signin")

                        .usernameParameter("email")
                        .passwordParameter("password")

                        .defaultSuccessUrl("/", true)
                        .failureUrl("/auth/signin?error")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/auth/signin?logout")
                        .permitAll()
                );

        return http.build();
    }
}