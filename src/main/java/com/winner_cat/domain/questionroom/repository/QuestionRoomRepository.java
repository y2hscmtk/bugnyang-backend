package com.winner_cat.domain.questionroom.repository;

import com.winner_cat.domain.questionroom.entity.QuestionRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRoomRepository extends JpaRepository<QuestionRoom,Long> {
}
