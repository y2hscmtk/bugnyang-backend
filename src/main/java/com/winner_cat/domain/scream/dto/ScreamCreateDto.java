package com.winner_cat.domain.scream.dto;

import com.winner_cat.domain.scream.entity.Scream;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

public class ScreamCreateDto {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Req {
        @NotBlank(message = "아우성을 작성하세요.")
        private String content;

        public Scream toEntity() {
            return Scream.builder()
                    .content(content)
                    .build();
        }
    }

    // 게시글 작성
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateScream {
        private Long screamId;
        private LocalDateTime updatedAt;

        public CreateScream(Long screamId, LocalDateTime updatedAt) {
            this.screamId = screamId;
            this.updatedAt = updatedAt;
        }
    }
}
