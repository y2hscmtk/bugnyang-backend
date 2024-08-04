package com.winner_cat.domain.scrap.controller;

import com.winner_cat.domain.scrap.service.ScrapService;
import com.winner_cat.global.jwt.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 내가 스크랩한 게시글 보기
     */
    @GetMapping("/mine")
    public ResponseEntity<?> getAllMyScrapArticles(
            @AuthenticationPrincipal CustomUserDetails userDetails, Pageable pageable) {
        return scrapService.getAllMyScrapArticles(userDetails.getEmail(), pageable);
    }

    /**
     * 태그로 내가 스크랩한 게시글 조회(미리보기)
     */
    @GetMapping("/mine/tag")
    public ResponseEntity<?> getAllMyScrapArticlesByTag(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam String tagName,
            Pageable pageable) {
        return scrapService.getAllMyScrapArticlesByTag(userDetails.getEmail(), tagName, pageable);
    }

    /**
     * 스크랩 취소
     */
    @DeleteMapping("/cancel/{articleId}")
    public ResponseEntity<?> cancelScrap(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(name = "articleId") Long articleId) {
        String email = userDetails.getEmail();
        return scrapService.cancelScrap(email, articleId);
    }
}
