package com.winner_cat.domain.questionroom.entity;

import com.winner_cat.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Question")
public class Question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @Column(length = 10000) // 최대 글자수 제한 10000자
    private String content; // 질문 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_room_id")
    private QuestionRoom questionRoom;
}
