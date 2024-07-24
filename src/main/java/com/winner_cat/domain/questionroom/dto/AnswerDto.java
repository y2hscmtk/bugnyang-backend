package com.winner_cat.domain.questionroom.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AnswerDto {
    private String content; // 답변 내용
    private LocalDateTime createdAt; // 생성 시간
}
