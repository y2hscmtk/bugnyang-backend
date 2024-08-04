package com.winner_cat.global.oauth2.handler;

import com.winner_cat.global.oauth2.dto.CustomOAuth2User;
import com.winner_cat.global.jwt.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomSuccessHandler.class);
    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        logger.info("Authentication Success Handler Called");

        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String username = customUserDetails.getUsername();
        System.out.println("username = " + username);
        String email = customUserDetails.getEmail();
        System.out.println("email = " + email);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(username,email,role);
        response.addCookie(createCookie("Authorization", token)); // 쿠키에 토큰 삽입
        response.addCookie(createCookie("email",email)); // 쿠키에 이메일 삽입
        response.addHeader("Authorization", "Bearer " + token); // 헤더에 쿠키 삽입

        logger.info("JWT Token Created and Added to Response");

        String targetUrl = "https://bugnyang2.netlify.app/"; // 메인 페이지로 리다이렉트
        getRedirectStrategy().sendRedirect(request, response, targetUrl);

        logger.info("Redirected to: " + targetUrl);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 60);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        return cookie;
    }
}
