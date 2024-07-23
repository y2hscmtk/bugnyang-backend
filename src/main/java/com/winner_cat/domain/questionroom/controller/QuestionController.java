package com.winner_cat.domain.questionroom.controller;

import com.winner_cat.domain.questionroom.dto.QuestionRequestDTO;
import com.winner_cat.domain.questionroom.service.QuestionService;
import com.winner_cat.global.jwt.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/question")
public class QuestionController {
    private final QuestionService questionService;

    /**
     * 질문 방에서 질문하기
     */
    @PostMapping
    public ResponseEntity<?> askQuestion(@RequestBody QuestionRequestDTO questionRequestDTO) {
        return questionService.askQuestion(questionRequestDTO);
    }

    /**
     * 새로운 질문 시작하기
     * 질문방 생성 후, 새로운 질문을 바탕으로 질문방 제목과 첫번째 답변 생성하여 반환
     */
    @PostMapping("/new")
    public ResponseEntity<?> askQuestion(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody String question) {
        String email = userDetails.getEmail();
        return questionService.startNewQuestion(email,question);
    }
}
