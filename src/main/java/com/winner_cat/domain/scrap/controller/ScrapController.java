package com.winner_cat.domain.scrap.controller;

import com.winner_cat.domain.scrap.service.ScrapService;
import com.winner_cat.global.jwt.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scrap")
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;

    /**
     * 게시글 스크랩하기
     */
    @PostMapping("{articleId}")
    public ResponseEntity<?> scrapArticle(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("articleId") Long articleId) {
        String email = userDetails.getEmail();
        return scrapService.scrapArticle(email, articleId);
    }
}
