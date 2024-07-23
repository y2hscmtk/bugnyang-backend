package com.winner_cat.domain.article.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ArticleListDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArticleResponse {
        private String title; // 게시글 제목
        @Builder.Default
        private List<String> tags = new ArrayList<>(); // 태그
        private String cause; // 원인
        private String solution; // 해결 방법
        private LocalDateTime updatedAt;

    }

    // 게시글 조회
    @Getter @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchArticleRes {
        private List<ArticleListDto.ArticleResponse> articles;

    }
}
