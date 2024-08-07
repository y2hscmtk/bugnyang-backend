package com.winner_cat.domain.questionroom.dto;

import com.winner_cat.domain.questionroom.entity.enums.QuestionState;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class NewQuestionResponseDTO {
    private Long questionRoomId;
    private String title; // 생성된 질문방 이름
    private String answer; // 생성된 답변
}
