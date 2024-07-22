package com.winner_cat.global.gpt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatCompletionDto {
    private String model;
    private List<ChatRequestMsgDto> messages;
}
