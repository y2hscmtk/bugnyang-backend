package com.winner_cat.domain.questionroom.repository;

import com.winner_cat.domain.questionroom.entity.Question;
import com.winner_cat.domain.questionroom.entity.QuestionRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question,Long> {
    List<Question> findTop5ByQuestionRoomOrderByCreatedAtDesc(QuestionRoom questionRoom);
}
