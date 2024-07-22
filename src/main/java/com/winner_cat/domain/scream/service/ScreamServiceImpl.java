package com.winner_cat.domain.scream.service;

import com.winner_cat.domain.scream.dto.ScreamCreateDto;
import com.winner_cat.domain.scream.entity.Scream;
import com.winner_cat.domain.scream.repository.ScreamRepository;
import com.winner_cat.global.response.ApiResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


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
}
