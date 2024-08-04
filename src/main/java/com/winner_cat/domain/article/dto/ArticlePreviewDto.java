package com.winner_cat.domain.article.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

// 아이디, 제목, 태그

public class ArticlePreviewDto {
    /**
     * 일반 게시글 미리보기(내가 스크랩한, 전체 게시글, 내가 작성한)
     */
    @Data
    @Builder
    public static class AllArticlePreviewResponse {
        private int totalPages;
        @Builder.Default
        private List<AllArticlePreview> articlePreviewList = new ArrayList<>();
    }

    @Data
    @Builder
    public static class AllArticlePreview {
        private Long articleId;
        private String title;
        @Builder.Default
        private List<TagResponseDto> tagList = new ArrayList<>();
    }


    /**
     * 태그로 게시글 미리보기 반환(메인페이지 로직)
     */
    @Data
    @Builder
    public static class TagArticlePreviewResponse {
        private int totalPages;
        @Builder.Default
        private List<TagArticlePreview> articlePreviewList = new ArrayList<>();
    }

    @Data
    @Builder
    public static class TagArticlePreview {
        private Long articleId;
        private String title;
        private String cause; // 원인
    }

}
