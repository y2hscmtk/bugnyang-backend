package com.winner_cat.domain.questionroom.controller;

import com.winner_cat.domain.questionroom.dto.QuestionRequestDTO;
import com.winner_cat.domain.questionroom.service.QuestionRoomService;
import com.winner_cat.global.jwt.dto.CustomUserDetails;
import com.winner_cat.global.jwt.service.CustomUserDetailsService;
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
public class QuestionRoomController {
    private final QuestionRoomService questionRoomService;

    @PostMapping
    public ResponseEntity<?> askQuestion(@RequestBody QuestionRequestDTO questionRequestDTO) {
        return questionRoomService.askQuestion(questionRequestDTO);
    }

    @PostMapping("/new")
    public ResponseEntity<?> askQuestion(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody String question) {
        String email = userDetails.getEmail();
        return questionRoomService.startNewQuestion(email,question);
    }
}
