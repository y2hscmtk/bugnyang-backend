package com.winner_cat.domain.questionroom.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewQuestionResponseDTO {
    private Long questionRoomId;
    private String title; // 생성된 질문방 이름
    private String answer; // 생성된 답변
}
