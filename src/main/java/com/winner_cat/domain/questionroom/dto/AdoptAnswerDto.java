package com.winner_cat.domain.questionroom.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdoptAnswerDto {
    private Long questionRoomId; // 채택하려는 질문방의 아이디
    private Long answerId; // 채택하려는 답변의 아이디
}
