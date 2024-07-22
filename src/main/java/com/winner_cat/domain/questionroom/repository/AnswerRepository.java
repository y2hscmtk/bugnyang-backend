package com.winner_cat.domain.questionroom.repository;

import com.winner_cat.domain.questionroom.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer,Long> {
}
