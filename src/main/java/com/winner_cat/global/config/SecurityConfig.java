package com.winner_cat.global.config;

import com.winner_cat.global.jwt.filter.JWTFilter;
import com.winner_cat.global.jwt.service.CustomUserDetailsService;
import com.winner_cat.global.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

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

        // JWT 검증 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
        http
                .addFilterBefore(new JWTFilter(customUserDetailsService, jwtUtil),
                        UsernamePasswordAuthenticationFilter.class);

        // 시큐리티 예외처리 필터
//        http
//                .exceptionHandling(exceptionHandling -> exceptionHandling
//                        .authenticationEntryPoint()
//                        .accessDeniedHandler());

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
