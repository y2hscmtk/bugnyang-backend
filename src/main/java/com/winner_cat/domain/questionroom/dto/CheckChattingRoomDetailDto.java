package com.winner_cat.domain.questionroom.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

/**
 * 질문방 상세 보기 반환 DTO
 * 해당 채팅방에서 나눈 질문과 대화를 생성된 시간 순으로 반환한다.
 */
@Data
@Builder
public class CheckChattingRoomDetailDto {
    @Builder.Default
    ArrayList<QuestionDto> questionList = new ArrayList<>();
    @Builder.Default
    ArrayList<AnswerDto> answerList = new ArrayList<>();
}
