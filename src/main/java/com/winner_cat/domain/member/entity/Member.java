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
    @Column(unique = true)
    private String username; // 서비스 사용자 고유 식별 키
    private String email; // JWT 검증 등에서 사용될 사용자 이름
    private String password;
    private String nickname; // 서비스에서 사용할 사용자의 이름
    private String role; // 사용자 권한

    public void changeEmail(String email) {
        this.email = email;
    }

    public void changeName(String name) {
        this.nickname = name;
    }
}
