package com.winner_cat.domain.article.controller;

import com.winner_cat.domain.article.dto.ArticleCreateDto;
import com.winner_cat.domain.article.dto.ArticleUpdateDto;
import com.winner_cat.domain.article.service.ArticleService;
import com.winner_cat.global.jwt.dto.CustomUserDetails;
import com.winner_cat.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
            @PathVariable Long articleId,
            @RequestBody ArticleUpdateDto.Req req) {
            ResponseEntity<ApiResponse<?>> result = articleService.modifyArticle(articleId, req);
            return result;
    }

    // 게시글 삭제
    @DeleteMapping("/{articleId}")
    public ResponseEntity<ApiResponse<?>> deleteArticle(
            @PathVariable Long articleId) {
        ResponseEntity<ApiResponse<?>> result = articleService.deleteArticle(articleId);
        return result;
    }

    // 게시글 상세 조회
    @GetMapping("/detail/{articleId}")
    public ResponseEntity<ApiResponse<?>> getArticleDetail(
            @PathVariable("articleId") Long articleId ) {
        ResponseEntity<ApiResponse<?>> result = articleService.getArticleDetail(articleId);
        return result;
    }

    // 내가 작성한 게시글 조회 (미리보기)
    @GetMapping("/mine")
    public ResponseEntity<ApiResponse<?>> getMyArticles(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String email = customUserDetails.getEmail();
        ResponseEntity<ApiResponse<?>> result = articleService.getMyArticles(email);
        return result;
    }

    // 전체 게시글 조회 - 미리보기
    @GetMapping("/all")
    public ResponseEntity<?> getAllArticle(Pageable pageable) {
        return articleService.getAllArticle(pageable);
    }
}
