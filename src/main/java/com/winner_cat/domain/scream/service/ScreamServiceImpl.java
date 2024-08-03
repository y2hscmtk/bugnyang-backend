package com.winner_cat.domain.scream.service;

import com.winner_cat.domain.scream.dto.ScreamCreateDto;
import com.winner_cat.domain.scream.dto.ScreamListDto;
import com.winner_cat.domain.scream.entity.Scream;
import com.winner_cat.domain.scream.repository.ScreamRepository;
import com.winner_cat.global.response.ApiResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Builder
public class ScreamServiceImpl implements ScreamService {
    private final ScreamRepository screamRepository;

    // 아우성 작성
    @Override
    public ResponseEntity<ApiResponse<?>> createScream(ScreamCreateDto.Req req){
        Scream scream = req.toEntity();
        Scream savedScream = screamRepository.save(scream);

        // 생성된 아우성의 정보를 포함한 응답 반환
        ScreamCreateDto.CreateScream createScreamResponse = new ScreamCreateDto.CreateScream(savedScream.getId(), savedScream.getUpdatedAt());
        ApiResponse<ScreamCreateDto.CreateScream> res = ApiResponse.onSuccess(createScreamResponse);
        return ResponseEntity.ok(res);
    }

    // 아우성 조회
    public ResponseEntity<ApiResponse<?>> getAllScreams(Long minutesAgo) {
        // 오늘 날짜의 시작과 끝을 정의
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        List<Scream> screams = screamRepository.findAllByUpdatedAtBetween(startOfDay, endOfDay);
        List<ScreamListDto.ScreamResponse> screamResponses = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (Scream scream : screams) {
            long minutesDifference = Duration.between(scream.getUpdatedAt(), now).toMinutes();
            String timeAgo;
            if (minutesDifference < 60) {
                timeAgo = minutesDifference + "분 전";
            } else {
                long hoursDifference = minutesDifference / 60;
                timeAgo = hoursDifference + "시간 전";
            }
            screamResponses.add(ScreamListDto.ScreamResponse.builder()
                    .content(scream.getContent())
                    .updatedAt(scream.getUpdatedAt())
                    .minutesAgo(minutesDifference)
                    .timeAgo(timeAgo)
                    .build());
        }

        ScreamListDto.SearchScreamsRes searchScreamsRes = new ScreamListDto.SearchScreamsRes(screamResponses);
        return ResponseEntity.ok(ApiResponse.onSuccess(searchScreamsRes));
    }


}
