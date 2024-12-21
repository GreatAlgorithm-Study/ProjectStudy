package com.projectStudy.kkogitcogit.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless 서버 설정
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호를 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll() // h2
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // swagger
                        .anyRequest().authenticated())
                .headers(header -> header.frameOptions(
                        HeadersConfigurer.FrameOptionsConfig::sameOrigin)) // 같은 도메인에 한해서 X-frame 허용
                .httpBasic(AbstractHttpConfigurer::disable) // httpBasic 비활성화
                .formLogin(AbstractHttpConfigurer::disable) // formLogin 비활성화
        ;
        return http.build();
    }
}
