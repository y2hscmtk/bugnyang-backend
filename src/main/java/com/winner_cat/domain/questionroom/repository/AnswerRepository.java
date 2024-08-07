package com.winner_cat.domain.questionroom.repository;

import com.winner_cat.domain.questionroom.entity.Answer;
import com.winner_cat.domain.questionroom.entity.QuestionRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer,Long> {
    List<Answer> findTop5ByQuestionRoomOrderByCreatedAtDesc(QuestionRoom questionRoom);
    // 시간 순으로 질문방에 소속된 답변들 얻어오기
    List<Answer> findByQuestionRoomOrderByCreatedAt(QuestionRoom questionRoom);
}
