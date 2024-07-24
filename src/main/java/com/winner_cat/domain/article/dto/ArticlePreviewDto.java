package com.winner_cat.domain.article.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

// 아이디, 제목, 태그

public class ArticlePreviewDto {
    @Data
    @Builder
    public static class AllArticlePreview {
        private Long articleId;
        private String title;
        @Builder.Default
        private List<String> tagList = new ArrayList<>();
    }

    @Data
    @Builder
    public static class TagArticlePreview {
        private Long articleId;
        private String title;
    }

}
