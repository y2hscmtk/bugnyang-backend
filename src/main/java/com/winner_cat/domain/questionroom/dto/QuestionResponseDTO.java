package com.winner_cat.domain.questionroom.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionResponseDTO {
    private Long answerId;
    private String answer;
}
