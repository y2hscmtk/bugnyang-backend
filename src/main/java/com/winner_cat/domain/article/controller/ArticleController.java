package com.winner_cat.domain.article.controller;

import com.winner_cat.domain.article.dto.ArticleCreateDto;
import com.winner_cat.domain.article.service.ArticleService;
import com.winner_cat.global.jwt.dto.CustomUserDetails;
import com.winner_cat.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createArticle(
            @RequestBody ArticleCreateDto.Req req,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String email = customUserDetails.getEmail();
        ResponseEntity<ApiResponse<?>> result = articleService.createArticle(req,email);
        return result;
    }
}
