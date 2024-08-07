package com.winner_cat.domain.questionroom.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuestionRequestDTO {
    @NotNull(message = "질문방 아이디가 필요합니다.")
    private Long questionRoomId;
    @NotNull(message = "질문이 비어있습니다.")
    private String question;
}
