package com.winner_cat.domain.questionroom.dto;

import com.winner_cat.domain.questionroom.entity.enums.QuestionState;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangeQuestionRoomStateDTO {
    private Long questionRoomId;
    private QuestionState state;
}
