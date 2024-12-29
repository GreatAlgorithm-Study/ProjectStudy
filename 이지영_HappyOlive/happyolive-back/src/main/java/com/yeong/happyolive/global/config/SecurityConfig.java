package com.yeong.happyolive.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeong.happyolive.auth.repository.UserRepository;
import com.yeong.happyolive.global.jwt.filter.JwtAuthenticationFilter;
import com.yeong.happyolive.global.jwt.filter.JwtExceptionFilter;
import com.yeong.happyolive.global.jwt.service.JwtService;
import com.yeong.happyolive.global.oauth2.handler.OAuth2LoginFailureHandler;
import com.yeong.happyolive.global.oauth2.handler.OAuth2LoginSuccessHandler;
import com.yeong.happyolive.global.oauth2.service.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * 인증은 CustomJsonUsernamePasswordAuthenticationFilter에서 authenticate()로 인증된 사용자로 처리
 * JwtAuthenticationProcessingFilter는 AccessToken, RefreshToken 재발급
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

    //    private final LoginService loginService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final OAuth2UserService OAuth2UserService;
    private final JwtExceptionFilter jwtExceptionFilter;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(form -> form.disable()
                )   // formLogin 사용 X
//                .httpBasic(AbstractHttpConfigurer::disable
//                )   // httpBasic 사용 X
//                .cors((cors) -> cors.disable())
                .csrf( (csrf) -> csrf.disable())   // csrf 사용 X
                .headers((headersConfig) ->
                        headersConfig.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable
                        )
                )
                .sessionManagement((httpSecuritySessionManagementConfigurer) ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ) // 세션 사용하지 않으므로 STATELESS로 설정

                //== URL별 권한 관리 옵션 ==//
                // 아이콘, css, js 관련
                // 기본 페이지, css, image, js 하위 폴더에 있는 자료들은 모두 접근 가능
                .authorizeHttpRequests((authorize) ->
                                authorize
                                        .requestMatchers("/**","/css/**","/images/**","/js/**","/favicon.ico").permitAll()
                                        .anyRequest().authenticated() // 위의 경로 이외에는 모두 인증된 사용자만 접근 가능
                )
                //== 소셜 로그인 설정 ==//
                .oauth2Login((httpSecurityOAuth2LoginConfigurer) ->
                        httpSecurityOAuth2LoginConfigurer
                                .successHandler(oAuth2LoginSuccessHandler) // 동의하고 계속하기를 눌렀을 때 Handler 설정
                                .failureHandler(oAuth2LoginFailureHandler) // 소셜 로그인 실패 시 핸들러 설정
                                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(OAuth2UserService))

                );
        // 원래 스프링 시큐리티 필터 순서가 LogoutFilter 이후에 로그인 필터 동작
        // 따라서, LogoutFilter 이후에 우리가 만든 필터 동작하도록 설정
        // 순서 : LogoutFilter -> JwtAuthenticationProcessingFilter ->
//        http.addFilterAfter(new JwtAuthenticationFilter(), jwtAuthenticationProcessingFilter());
//        http.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
//        http.addFilterBefore(jwtAuthenticationProcessingFilter(), LoginAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtExceptionFilter,JwtAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    public JwtAuthenticationFilter jwtAuthenticationProcessingFilter() {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService, userRepository);
        return jwtAuthenticationFilter;
    }



}
