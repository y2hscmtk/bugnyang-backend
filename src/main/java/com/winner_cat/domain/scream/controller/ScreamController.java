package com.winner_cat.domain.scream.controller;

import com.winner_cat.domain.scream.dto.ScreamCreateDto;
import com.winner_cat.domain.scream.service.ScreamService;
import com.winner_cat.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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

    // 아우성 조회
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllScreams(@RequestParam(required = false) Long minutesAgo) {
        if (minutesAgo == null) {
            minutesAgo = 60L; // 기본값 60분
        }
        return screamService.getAllScreams(minutesAgo);
    }


}
