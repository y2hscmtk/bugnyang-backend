package com.winner_cat.global.gpt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatCompletionDto {
    private String model;

    @JsonProperty("messages") // 직렬화
    private List<ChatRequestMsgDto> messages;

    // JSON 요청 본문 생성을 위한 메서드
    public Map<String, Object> toRequestBody() {
        return Map.of(
                "model", this.model,
                "messages", this.messages
        );
    }
}
