package com.winner_cat.domain.scrap.dto;

import com.winner_cat.domain.article.dto.TagResponseDto;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class ScrapArticlePreview {
    @Data
    @Builder
    public static class AllArticlePreviewResponse {
        private int totalPages;
        @Builder.Default
        private List<AllArticlePreview> scrapPreviewList = new ArrayList<>();
    }

    @Data
    @Builder
    public static class AllArticlePreview {
        private Long articleId;
        private String title;
        @Builder.Default
        private List<TagResponseDto> tagList = new ArrayList<>();
    }
}