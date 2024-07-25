package com.winner_cat.domain.questionroom.controller;

import com.winner_cat.domain.questionroom.dto.ChangeQuestionRoomStateDTO;
import com.winner_cat.domain.questionroom.service.QuestionRoomService;
import com.winner_cat.global.jwt.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/question-room")
@RequiredArgsConstructor
public class QuestionRoomController {
    private final QuestionRoomService questionRoomService;
    /**
     * 질문방 미리보기
     * 특정 회원이 생성한 질문방의 제목과, 답변 상태, 최종 수정 시간을 반환한다.
     */
    @GetMapping
    public ResponseEntity<?> getQuestionRoomPreview(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return questionRoomService.getQuestionRoomPreview(customUserDetails);
    }

    /**
     * 질문방 상세보기
     * 특정 질문 방에서 나눈 대화 반환
     */
    @GetMapping("/{questionRoomId}")
    public ResponseEntity<?> getQuestionRoomDetail(
            @PathVariable("questionRoomId") Long questionRoomId) {
        return questionRoomService.getQuestionRoomDetail(questionRoomId);
    }

    /**
     * 질문방 상태 변경
     */
    @PatchMapping("/state")
    public ResponseEntity<?> changeState(
            @RequestBody ChangeQuestionRoomStateDTO requestDto) {
        return questionRoomService
                .changeState(requestDto.getQuestionRoomId(), requestDto.getState());
    }
}
