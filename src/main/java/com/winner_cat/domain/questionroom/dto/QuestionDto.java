package com.winner_cat.domain.questionroom.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class QuestionDto {
    private String content; // 질문 내용
    private LocalDateTime createdAt; // 생성 시간
}
