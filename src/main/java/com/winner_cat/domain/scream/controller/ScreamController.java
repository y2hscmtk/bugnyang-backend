package com.winner_cat.domain.scream.controller;

import com.winner_cat.domain.scream.dto.ScreamCreateDto;
import com.winner_cat.domain.scream.service.ScreamService;
import com.winner_cat.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/scream")
@RequiredArgsConstructor
public class ScreamController {
    private final ScreamService screamService;

    // 아우성 작성
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createScream(
            @Valid @RequestBody ScreamCreateDto.Req req) {
        ResponseEntity<ApiResponse<?>> result = screamService.createScream(req);
        return result;
    }
}
