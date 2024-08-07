package com.winner_cat.domain.article.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class TodayErrorDto {
    @Data
    @Builder
    public static class ErrorCount {
        private Long totalCount;
        @Builder.Default
        private List<ErrorDto> ranking = new ArrayList<>();
    }

    @Data
    @Builder
    public static class ErrorDto {
        private String tagName;
        private Long count;
    }
}


