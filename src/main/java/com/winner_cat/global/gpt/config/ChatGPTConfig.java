package com.winner_cat.global.gpt.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class ChatGPTConfig {
    @Value("${spring.gpt.secret}")
    private String secretKey;

    @Value("${openai.url.prompt}")
    private String promptUrl; // gpt api 엔드포인트에 해당
}