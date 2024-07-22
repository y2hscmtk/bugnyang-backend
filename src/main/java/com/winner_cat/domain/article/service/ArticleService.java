package com.winner_cat.domain.article.service;

import com.winner_cat.domain.article.dto.ArticleCreateDto;
import com.winner_cat.domain.article.dto.ArticleUpdateDto;
import com.winner_cat.global.jwt.dto.CustomUserDetails;
import com.winner_cat.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

public interface ArticleService {
    ResponseEntity<ApiResponse<?>> createArticle(ArticleCreateDto.Req req,String email);
    ResponseEntity<ApiResponse<?>> modifyArticle(Integer articleId, ArticleUpdateDto.Req req);
}
