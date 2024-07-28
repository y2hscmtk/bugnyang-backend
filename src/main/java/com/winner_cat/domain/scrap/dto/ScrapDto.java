package com.winner_cat.domain.scrap.dto;

import com.winner_cat.domain.article.dto.TagResponseDto;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 게시글 아이디, 제목, 태그들
 */
public class ScrapDto {

    @Builder
    @Data
    public static class ScrapPreviewResponseDto {
        private int totalPages;
        @Builder.Default
        private List<ScrapPreviewDto> scrapPreviewList = new ArrayList<>();
    }


    @Builder
    @Data
    public static class ScrapPreviewDto {
        private Long articleId;
        private String title;
        private List<TagResponseDto> tags;
    }
}
