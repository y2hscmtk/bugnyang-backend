package com.winner_cat.domain.article.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ArticleUpdateDto {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Req {
        private String title; // 제목
        private List<String> tags = new ArrayList<>(); // 태그
        private String cause; // 원인
        private String solution; // 해결방법
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UpdateArticle {
        private LocalDateTime updatedAt;

        public UpdateArticle(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
        }
    }

}
