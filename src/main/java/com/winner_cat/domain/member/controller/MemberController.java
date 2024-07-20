package com.winner_cat.domain.member.controller;

import com.winner_cat.domain.member.dto.JoinDTO;
import com.winner_cat.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<?> joinProcess(@RequestBody JoinDTO joinDTO) {
        // 회원 가입 진행
        return memberService.join(joinDTO);
    }
}
