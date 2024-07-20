package com.winner_cat.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Member")
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username; // JWT 검증 등에서 사용될 사용자 이름
    private String nickname; // 서비스에서 사용할 사용자의 이름
    private String email; // 사용자 이메일
}
