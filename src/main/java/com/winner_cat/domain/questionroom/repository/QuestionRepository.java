package com.winner_cat.domain.questionroom.repository;

import com.winner_cat.domain.questionroom.entity.Question;
import com.winner_cat.domain.questionroom.entity.QuestionRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question,Long> {
    List<Question> findTop5ByQuestionRoomOrderByCreatedAtDesc(QuestionRoom questionRoom);
    // 시간 순으로 질문방에 소속된 질문들 얻어오기
    List<Question> findByQuestionRoomOrderByCreatedAt(QuestionRoom questionRoom);
}
