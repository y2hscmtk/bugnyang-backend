package com.winner_cat.domain.member.controller;

import com.winner_cat.global.jwt.dto.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    /**
     * JWT Test
     */
    @GetMapping("/test")
    public String test(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return "Welcome" + userDetails.getEmail();
    }

    /**
     * CI Test
     */
    @GetMapping("/ci")
    public String ci() {
        return "ci success!";
    }
}
