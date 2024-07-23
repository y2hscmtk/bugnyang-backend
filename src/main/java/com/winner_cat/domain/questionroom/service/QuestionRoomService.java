package com.winner_cat.domain.questionroom.service;

import com.winner_cat.domain.questionroom.entity.QuestionRoom;
import com.winner_cat.domain.questionroom.entity.enums.QuestionState;
import com.winner_cat.domain.questionroom.repository.AnswerRepository;
import com.winner_cat.domain.questionroom.repository.QuestionRepository;
import com.winner_cat.domain.questionroom.repository.QuestionRoomRepository;
import com.winner_cat.global.enums.statuscode.ErrorStatus;
import com.winner_cat.global.exception.GeneralException;
import com.winner_cat.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionRoomService {
    private final QuestionRoomRepository questionRoomRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    /**
     * 질문 방 상태 수정
     */
    public ResponseEntity<?> changeState(Long questionRoomId,QuestionState state) {
        // 1. 질문방 존재하는지 확인
        QuestionRoom questionRoom = questionRoomRepository.findById(questionRoomId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.QUESTION_ROOM_NOT_FOUND));

        // 2. 상태 변경
        questionRoom.changeState(state);

        return ResponseEntity.ok().body(ApiResponse.onSuccess("상태 변경에 성공하였습니다."));
    }
}
