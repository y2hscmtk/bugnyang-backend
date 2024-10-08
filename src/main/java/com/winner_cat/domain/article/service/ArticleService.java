package com.winner_cat.domain.article.service;

import com.winner_cat.domain.article.dto.ArticleCreateDto;
import com.winner_cat.domain.article.dto.ArticleUpdateDto;
import com.winner_cat.global.jwt.dto.CustomUserDetails;
import com.winner_cat.global.response.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

public interface ArticleService {
    ResponseEntity<ApiResponse<?>> createArticle(ArticleCreateDto.Req req,String email);
    ResponseEntity<ApiResponse<?>> getArticleDetail(String email, Long articleId);
    ResponseEntity<ApiResponse<?>> modifyArticle(Long articleId, ArticleUpdateDto.Req req, String email);
    ResponseEntity<ApiResponse<?>> deleteArticle(Long articleId, String email);
    ResponseEntity<ApiResponse<?>> getMyArticles(String email, Pageable pageable);
    ResponseEntity<?> getAllArticle(Pageable pageable);
    ResponseEntity<?> getArticleByTag(String tagName, Pageable pageable);
    ResponseEntity<?> getMyArticleByTag(String email, String tagName, Pageable pageable);
    ResponseEntity<?> getArticleRecommendByTag(String tagName, Pageable pageable);
    ResponseEntity<?> getTodayFixErrorInfo();
}
