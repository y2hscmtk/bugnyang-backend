package com.winner_cat.domain.questionroom.controller;

import com.winner_cat.domain.questionroom.dto.QuestionRequestDTO;
import com.winner_cat.domain.questionroom.service.QuestionRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}
