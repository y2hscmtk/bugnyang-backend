package com.winner_cat.domain.questionroom.repository;

import com.winner_cat.domain.questionroom.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question,Long> {
}
