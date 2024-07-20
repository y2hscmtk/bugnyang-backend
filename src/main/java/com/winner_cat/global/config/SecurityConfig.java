package com.winner_cat.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // csrf disable
        http
                .csrf(auth -> auth.disable());
        // Form 로그인 방식, http basic 인증 방식 disable
        http
                .formLogin(auth -> auth.disable())
                .httpBasic(auth -> auth.disable());
        // 경로별 인가 설정
        http
                .authorizeHttpRequests(auth -> auth
                        // login, root, join 경로의 요청에 대해서는 모두 허용
                        .requestMatchers("/login", "/", "/join").permitAll()
                        // admin 경로 요청에 대해서는 ADMIN 권한을 가진 경우에만 허용
                        .requestMatchers("/admin").hasRole("ADMIN")
                        // 이외의 요청에 대해서는 인증된 사용자만 허용
                        .anyRequest().authenticated()
                );
        // JWT 방식에서 세션은 STATELESS 상태로 관리됨
        http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }


}
