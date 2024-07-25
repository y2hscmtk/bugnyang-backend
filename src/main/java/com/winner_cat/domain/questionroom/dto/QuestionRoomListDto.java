package com.winner_cat.domain.questionroom.dto;

import com.winner_cat.domain.questionroom.entity.enums.QuestionState;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class QuestionRoomListDto {
    @Getter @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class QuestionRoomResponse {
        private String roomName;
        private QuestionState state;
        private LocalDateTime updatedAt;
    }
}
