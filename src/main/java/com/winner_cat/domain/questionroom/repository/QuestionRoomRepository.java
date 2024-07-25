package com.winner_cat.domain.questionroom.repository;

import com.winner_cat.domain.member.entity.Member;
import com.winner_cat.domain.questionroom.entity.QuestionRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRoomRepository extends JpaRepository<QuestionRoom,Long> {
    List<QuestionRoom> findQuestionRoomByMember(Member member);
}
