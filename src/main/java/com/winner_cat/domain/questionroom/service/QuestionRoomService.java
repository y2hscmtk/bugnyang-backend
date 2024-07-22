package com.winner_cat.domain.questionroom.service;

import com.winner_cat.domain.questionroom.dto.QuestionRequestDTO;
import com.winner_cat.domain.questionroom.entity.Answer;
import com.winner_cat.domain.questionroom.entity.Question;
import com.winner_cat.domain.questionroom.entity.QuestionRoom;
import com.winner_cat.domain.questionroom.repository.AnswerRepository;
import com.winner_cat.domain.questionroom.repository.QuestionRepository;
import com.winner_cat.domain.questionroom.repository.QuestionRoomRepository;
import com.winner_cat.global.enums.statuscode.ErrorStatus;
import com.winner_cat.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionRoomService {
    private final QuestionRoomRepository questionRoomRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    /**
     * 질문방 아이디, 질문 내용
     */
    public ResponseEntity<?> askQuestion(QuestionRequestDTO requestDTO) {
        Long questionRoomId = requestDTO.getQuestionRoomId();
        String newQuestion = requestDTO.getQuestion();

        // 1. 실제로 질문방이 존재하는지 확인
        QuestionRoom questionRoom = questionRoomRepository.findById(questionRoomId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.QUESTION_ROOM_NOT_FOUND));

        // 2. 기존에 나눴던 대화를 바탕으로 대화 흐름 생성
        // 앞서 나눴던 대화 중 상위 5개씩 조회
        List<Question> recentQuestions = questionRepository.findTop5ByQuestionRoomOrderByCreatedAtDesc(questionRoom);
        List<Answer> recentAnswers = answerRepository.findTop5ByQuestionRoomOrderByCreatedAtDesc(questionRoom);

        // 3. 대화의 흐름 + 새로운 질문으로 GPT 답변 생성 요청

        // 4. 새로운 사용자 질문 저장, 질문에 대한 답변 저장

        // 5. DTO(답변 + 답변ID) 반환
        return null;
    }
}
