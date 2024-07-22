package com.winner_cat.domain.article.controller;

import com.winner_cat.domain.article.dto.ArticleCreateDto;
import com.winner_cat.domain.article.dto.ArticleUpdateDto;
import com.winner_cat.domain.article.service.ArticleService;
import com.winner_cat.global.jwt.dto.CustomUserDetails;
import com.winner_cat.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    // 게시글 작성
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createArticle(
            @RequestBody ArticleCreateDto.Req req,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String email = customUserDetails.getEmail();
        ResponseEntity<ApiResponse<?>> result = articleService.createArticle(req,email);
        return result;
    }

    // 게시글 수정
    @PatchMapping("/{articleId}")
    public ResponseEntity<ApiResponse<?>> modifyArticle(
            @PathVariable Integer articleId,
            @RequestBody ArticleUpdateDto.Req req) {
            ResponseEntity<ApiResponse<?>> result = articleService.modifyArticle(articleId, req);
            return result;
    }

}
