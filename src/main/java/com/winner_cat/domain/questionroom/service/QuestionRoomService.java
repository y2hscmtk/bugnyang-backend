package com.winner_cat.domain.questionroom.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winner_cat.domain.questionroom.dto.QuestionRequestDTO;
import com.winner_cat.domain.questionroom.dto.QuestionResponseDTO;
import com.winner_cat.domain.questionroom.entity.Answer;
import com.winner_cat.domain.questionroom.entity.Question;
import com.winner_cat.domain.questionroom.entity.QuestionRoom;
import com.winner_cat.domain.questionroom.repository.AnswerRepository;
import com.winner_cat.domain.questionroom.repository.QuestionRepository;
import com.winner_cat.domain.questionroom.repository.QuestionRoomRepository;
import com.winner_cat.global.enums.statuscode.ErrorStatus;
import com.winner_cat.global.exception.GeneralException;
import com.winner_cat.global.gpt.config.ChatGPTConfig;
import com.winner_cat.global.gpt.dto.ChatCompletionDto;
import com.winner_cat.global.gpt.dto.ChatRequestMsgDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionRoomService {
    private final QuestionRoomRepository questionRoomRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final RestTemplate restTemplate;
    private final ChatGPTConfig chatGPTConfig;

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
        // 대화 흐름 생성
        List<ChatRequestMsgDto> messages = new ArrayList<>();
        messages.add(new ChatRequestMsgDto("system", "당신은 프로그래밍 오류를 해결하기 위한 인공지능 입니다. 주어진 질문에 대해 짧고 명료하게 해결 방법을 안내 하세요."));
        messages.addAll(createMessagesFromHistory(recentQuestions, recentAnswers));
        messages.add(new ChatRequestMsgDto("user", newQuestion));

        // 3. 대화의 흐름 + 새로운 질문으로 GPT 답변 생성 요청
        ChatCompletionDto chatCompletionDto = ChatCompletionDto.builder()
                .model("gpt-4")
                .messages(messages)
                .build();
        String gptAnswer = callGptApi(chatCompletionDto);

        // 4. 새로운 사용자 질문 저장, 질문에 대한 답변 저장
        Question question = Question.builder()
                .content(newQuestion)
                .questionRoom(questionRoom)
                .build();
        questionRepository.save(question);

        Answer answer = Answer.builder()
                .content(gptAnswer)
                .questionRoom(questionRoom)
                .build();
        answerRepository.save(answer);

        // 5. DTO(답변 + 답변ID) 반환
        QuestionResponseDTO responseDTO = QuestionResponseDTO.builder()
                .answerId(answer.getId())
                .answer(gptAnswer)
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    private List<ChatRequestMsgDto> createMessagesFromHistory(List<Question> questions, List<Answer> answers) {
        List<ChatRequestMsgDto> messages = new ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {
            messages.add(new ChatRequestMsgDto("user", questions.get(i).getContent()));
            if (i < answers.size()) {
                messages.add(new ChatRequestMsgDto("assistant", answers.get(i).getContent()));
            }
        }

        // 대화 흐름 출력
        System.out.println("===== 대화 흐름 =====");
        messages.forEach(msg -> System.out.println(msg.getRole() + ": " + msg.getContent()));
        System.out.println("====================");

        return messages;
    }

    private String callGptApi(ChatCompletionDto chatCompletionDto) {
        HttpHeaders headers = chatGPTConfig.httpHeaders();

        HttpEntity<ChatCompletionDto> requestEntity = new HttpEntity<>(chatCompletionDto, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(chatGPTConfig.getPromptUrl(), requestEntity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                ObjectMapper om = new ObjectMapper();
                Map<String, Object> responseBody = om.readValue(response.getBody(), new TypeReference<>() {});
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                return (String) message.get("content");
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to parse GPT API response", e);
            }
        } else {
            throw new RuntimeException("Failed to call GPT API: " + response.getStatusCode());
        }
    }
}
