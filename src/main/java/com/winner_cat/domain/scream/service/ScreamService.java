package com.winner_cat.domain.scream.service;

import com.winner_cat.domain.scream.dto.ScreamCreateDto;
import com.winner_cat.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface ScreamService {
    ResponseEntity<ApiResponse<?>> createScream(ScreamCreateDto.Req req);
}
