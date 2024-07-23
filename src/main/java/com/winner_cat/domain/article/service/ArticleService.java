package com.winner_cat.domain.article.service;

import com.winner_cat.domain.article.dto.ArticleCreateDto;
import com.winner_cat.domain.article.dto.ArticleUpdateDto;
import com.winner_cat.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface ArticleService {
    ResponseEntity<ApiResponse<?>> createArticle(ArticleCreateDto.Req req,String email);
    ResponseEntity<ApiResponse<?>> modifyArticle(Long articleId, ArticleUpdateDto.Req req);
    ResponseEntity<ApiResponse<?>> deleteArticle(Long articleId);
    ResponseEntity<ApiResponse<?>> getArticleDetail(Long articleId );
}
