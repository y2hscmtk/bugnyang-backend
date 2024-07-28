package com.winner_cat.domain.article.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ArticleCreateDto {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Req {
        @NotBlank(message = "제목을 작성해주세요.")
        private String title; // 제목

        @NotBlank(message = "태그를 선택해주세요.")
        @Builder.Default
        private List<String> tags = new ArrayList<>();

        @NotBlank(message = "원인을 작성해주세요.")
        private String cause; // 원인

        @NotBlank(message = "해결방법을 작성해주세요.")
        private String solution; // 해결방법

    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateArticle{
        private Long articleId;
        private LocalDateTime updatedAt;

        public CreateArticle(Long articleId, LocalDateTime updatedAt) {
            this.articleId = articleId;
            this.updatedAt = updatedAt;
        }
    }


}
