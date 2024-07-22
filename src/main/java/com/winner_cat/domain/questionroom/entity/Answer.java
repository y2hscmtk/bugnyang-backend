package com.winner_cat.domain.questionroom.entity;

import com.winner_cat.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Answer")
public class Answer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    @Column(length = 2000) // 길이를 2000자로 설정
    private String content; // 답변 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_room_id")
    private QuestionRoom questionRoom;
}
