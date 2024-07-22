package com.winner_cat.domain.member.controller;

import com.winner_cat.domain.member.dto.JoinDTO;
import com.winner_cat.domain.member.dto.LoginRequestDTO;
import com.winner_cat.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO dto) {
        return memberService.login(dto);
    }

    /**
     * 회원 가입
     */
    @PostMapping("/join")
    public ResponseEntity<?> joinProcess(@RequestBody JoinDTO joinDTO) {
        return memberService.join(joinDTO);
    }
}
