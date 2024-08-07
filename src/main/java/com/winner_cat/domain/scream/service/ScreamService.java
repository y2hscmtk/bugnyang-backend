package com.winner_cat.domain.scream.service;

import com.winner_cat.domain.scream.dto.ScreamCreateDto;
import com.winner_cat.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

public interface ScreamService {
    ResponseEntity<ApiResponse<?>> createScream(ScreamCreateDto.Req req);
    ResponseEntity<ApiResponse<?>> getAllScreams(Long minutesAgo);
}
