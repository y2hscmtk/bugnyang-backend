package com.winner_cat.domain.scream.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class ScreamListDto {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class ScreamResponse {
        private String content; // 아우성
        private LocalDateTime updatedAt;
        private Long minutesAgo; // n분 전
        private String timeAgo; // n분 전 또는 n시간 전

        public void setMinutesAgo(Long minutesAgo) {
            this.minutesAgo = minutesAgo;
        }

        public void setTimeAgo(String timeAgo) {
            this.timeAgo = timeAgo;
        }
    }

    // 아우성 조회
    @Getter @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SearchScreamsRes {
        private List<ScreamResponse> screams;
        public SearchScreamsRes(List<ScreamListDto.ScreamResponse> screams) {
            this.screams = screams;
        }
    }
}


