package com.winner_cat.domain.questionroom.service;

import com.fasterxml.jackson.databind.JsonNode;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionRoomService {
    private final QuestionRoomRepository questionRoomRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final ChatGPTConfig chatGPTConfig;
    private final ObjectMapper objectMapper;

    public ResponseEntity<?> askQuestion(QuestionRequestDTO requestDTO) {
        Long questionRoomId = requestDTO.getQuestionRoomId();
        String newQuestion = requestDTO.getQuestion();

        // 1. 실제로 질문방이 존재하는지 확인
        QuestionRoom questionRoom = questionRoomRepository.findById(questionRoomId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.QUESTION_ROOM_NOT_FOUND));

        // 2. 기존에 나눴던 대화를 바탕으로 대화 흐름 생성
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

        // 최신순으로 정렬된 리스트를 뒤집어서 올바른 순서로 추가
        Collections.reverse(questions);
        Collections.reverse(answers);

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
        try {
            HttpClient client = HttpClient.newHttpClient();

            // JSON 요청 본문 생성
            Map<String, Object> requestBody = chatCompletionDto.toRequestBody();
            String requestBodyJson = objectMapper.writeValueAsString(requestBody);

            // 직렬화된 JSON 확인
            System.out.println("Request Body JSON: " + requestBodyJson);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(chatGPTConfig.getPromptUrl()))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + chatGPTConfig.getSecretKey())
                    .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 응답을 출력하여 형식을 확인
            System.out.println("GPT 응답 상태 코드: " + response.statusCode());
            System.out.println("GPT 응답 본문: " + response.body());

            if (response.statusCode() == 200) {
                JsonNode jsonNode = objectMapper.readTree(response.body());
                return jsonNode.path("choices").get(0).path("message").path("content").asText();
            } else {
                throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
        }
    }
}
