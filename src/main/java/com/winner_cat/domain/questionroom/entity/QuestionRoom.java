package com.winner_cat.domain.questionroom.entity;

import com.winner_cat.domain.questionroom.entity.enums.QuestionState;
import com.winner_cat.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Question_Room")
public class QuestionRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_room_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private QuestionState state; // 게시글 진행 상태
}
