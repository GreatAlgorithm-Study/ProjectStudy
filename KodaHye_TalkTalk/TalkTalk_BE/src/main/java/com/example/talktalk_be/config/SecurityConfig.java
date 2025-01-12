package com.example.talktalk_be.config;

import com.example.talktalk_be.domain.member.persistence.respository.MemberRepository;
import com.example.talktalk_be.domain.member.service.LoginService;
import com.example.talktalk_be.global.jwt.filter.JwtAuthenticationProcessingFilter;
import com.example.talktalk_be.global.jwt.service.JwtService;
import com.example.talktalk_be.global.login.filter.JsonUsernamePasswordAuthenticationFilter;
import com.example.talktalk_be.global.login.handler.LoginFailureHandler;
import com.example.talktalk_be.global.login.handler.LoginSuccessJWTProvideHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/*
인가 설정을 진행하는 클래스
 */

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final LoginService loginService;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable) // 이거 해줘야 post 403 오류 발생안함
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((request) -> request
                        .requestMatchers("/login").permitAll()
//                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/member/sign-up").permitAll() // 모든 사용자가 권한이 없어도 허용함
//                        .requestMatchers("admin").hasRole("ADMIN") // 어드민 페이지
//                        .requestMatchers("my/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated() // 그 외 페이지는 로그인이 되었다면 허용
                );

        http.addFilterAfter(jsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
        http.addFilterBefore(jwtAuthenticationProcessingFilter(), JsonUsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public LoginSuccessJWTProvideHandler loginSuccessJWTProvideHandler() {
        return new LoginSuccessJWTProvideHandler(jwtService, memberRepository);
    }

    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        // AuthenticationProvider을 지정하는데, FormLogin과 동일하게 DaoAuthenticationProvider 사용
        // DaoAuthenticationProvider 사용
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(loginService);
        // AuthenticationManager도 formLogin과 마찬가지로 ProviderManager 사용
        return new ProviderManager(provider);
    }

    @Bean
    public JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordAuthenticationFilter() {
        JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordLoginFilter = new JsonUsernamePasswordAuthenticationFilter(objectMapper);
        jsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
        jsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessJWTProvideHandler());
        jsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return jsonUsernamePasswordLoginFilter;
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        JwtAuthenticationProcessingFilter jsonUsernamePasswordLoginFilter = new JwtAuthenticationProcessingFilter(jwtService, memberRepository);
        return jsonUsernamePasswordLoginFilter;
    }
}
