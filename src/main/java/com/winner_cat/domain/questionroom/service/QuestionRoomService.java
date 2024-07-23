package com.winner_cat.domain.questionroom.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winner_cat.domain.member.entity.Member;
import com.winner_cat.domain.member.repository.MemberRepository;
import com.winner_cat.domain.questionroom.dto.NewQuestionResponseDTO;
import com.winner_cat.domain.questionroom.dto.QuestionRequestDTO;
import com.winner_cat.domain.questionroom.dto.QuestionResponseDTO;
import com.winner_cat.domain.questionroom.entity.Answer;
import com.winner_cat.domain.questionroom.entity.Question;
import com.winner_cat.domain.questionroom.entity.QuestionRoom;
import com.winner_cat.domain.questionroom.entity.enums.QuestionState;
import com.winner_cat.domain.questionroom.repository.AnswerRepository;
import com.winner_cat.domain.questionroom.repository.QuestionRepository;
import com.winner_cat.domain.questionroom.repository.QuestionRoomRepository;
import com.winner_cat.global.enums.statuscode.ErrorStatus;
import com.winner_cat.global.exception.GeneralException;
import com.winner_cat.global.gpt.config.ChatGPTConfig;
import com.winner_cat.global.gpt.dto.ChatCompletionDto;
import com.winner_cat.global.gpt.dto.ChatRequestMsgDto;
import com.winner_cat.global.response.ApiResponse;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionRoomService {
    private final QuestionRoomRepository questionRoomRepository;
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
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

        return ResponseEntity.ok(ApiResponse.onSuccess(responseDTO));
    }

    public ResponseEntity<?> startNewQuestion(String email, String question) {
        // 1. 회원이 실제로 존재하는지 확인
        Member member = memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        // 2. 새로운 질문방 생성
        QuestionRoom questionRoom = QuestionRoom.builder()
                .member(member)
                .state(QuestionState.PROGRESS)
                .build();

        QuestionRoom savedQuestionRoom = questionRoomRepository.save(questionRoom);

        List<ChatRequestMsgDto> messages = new ArrayList<>();
        String prompt = "당신은 프로그래밍 오류를 해결하기 위한 인공지능 입니다. "
                + "주어진 질문에 대해 짧고 명료하게 해결 방법을 안내 하세요."
                + "markdown 형식 말고 plain-text 형식으로 알려줘야 합니다."
                + "또한 주어진 질문의 답변에 대한 제목을 짧게 한줄로 제공해 주세요. 제목은 [ ]로 감싸서 적어주세요. 질문 답변형식은 예시와 달라도 괜찮습니다. 하지만 제목은 반드시 [ ]로 감싸져있어야 합니다."
                + "<예시>"
                + "[NullPointerException 해결 방법]\\n\\nNullPointerException이 발생하는 이유는 코드 내에 null 값을 참조하는 객체가 있기 때문입니다.\\n\\n해결 방법은 다음과 같습니다:\\n\\n1. 객체 생성 이후 혹은 초기화 블록에서 객체의 인스턴스 변수에 대해 초기화를 해야 합니다.\\n2. 메서드 호출 시, 객체가 null인지 아닌지를 확인해야 합니다. null이 아닐 때만 메서드를 호출해야 에러를 방지할 수 있습니다.\\n3. null을 반환하는 메서드를 사용할 때는 항상 반환값을 검사해야 합니다. 이 방법을 통해 NullPointerException을 방지할 수 있습니다. \\n\\n위의 방법을 통해 NullPointerException을 해결하고 예방할 수 있습니다.";
        messages.add(new ChatRequestMsgDto("system", prompt));
        messages.add(new ChatRequestMsgDto("user", question));

        ChatCompletionDto chatCompletionDto = ChatCompletionDto.builder()
                .model("gpt-4o")
                .messages(messages)
                .build();

        String gptAnswer = callGptApi(chatCompletionDto);
        System.out.println("===============");
        System.out.println("gptAnswer = " + gptAnswer);
        System.out.println("===============");
        // 제목과 답변으로 내용 분리
        // 정규표현식 패턴 정의
        String regex = "\\[(.*?)\\]\\s*(.*)";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(gptAnswer);

        String title = "";
        String content = "";

        if (matcher.find()) {
            title = matcher.group(1);
            content = matcher.group(2);
        } else {
            throw new IllegalStateException("No match found");
        }

        // 분리 후 질문방에 제목 지정
        savedQuestionRoom.changeName(title);

        System.out.println("==============");
        System.out.println("title = " + title);
        System.out.println("content = " + content);
        System.out.println("==============");

        // 3. 새로운 사용자 질문 저장, 질문에 대한 답변 저장
        Question questionEntity = Question.builder()
                .content(question)
                .questionRoom(savedQuestionRoom)
                .build();
        questionRepository.save(questionEntity);

        Answer answer = Answer.builder()
                .content(content)
                .questionRoom(savedQuestionRoom)
                .build();
        answerRepository.save(answer);

        // 응답 DTO 생성
        NewQuestionResponseDTO responseDTO = NewQuestionResponseDTO.builder()
                .questionRoomId(savedQuestionRoom.getId())
                .title(title)
                .answer(content)
                .build();

        return ResponseEntity.ok(ApiResponse.onSuccess(responseDTO));
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
//        System.out.println("===== 대화 흐름 =====");
//        messages.forEach(msg -> System.out.println(msg.getRole() + ": " + msg.getContent()));
//        System.out.println("====================");

        return messages;
    }

    private String callGptApi(ChatCompletionDto chatCompletionDto) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            // JSON 요청 본문 생성
            Map<String, Object> requestBody = chatCompletionDto.toRequestBody();
            String requestBodyJson = objectMapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(chatGPTConfig.getPromptUrl()))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + chatGPTConfig.getSecretKey())
                    .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 응답 형태 확인
//            System.out.println("GPT 응답 상태 코드: " + response.statusCode());
//            System.out.println("GPT 응답 본문: " + response.body());

            if (response.statusCode() == 200) {
                JsonNode jsonNode = objectMapper.readTree(response.body());
                return jsonNode.path("choices").get(0).path("message").path("content").asText();
            } else if(response.statusCode() == 400) {
                throw new GeneralException(ErrorStatus.FAIL_TO_CREATE_ANSWER);
            } else {
                throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
        }
    }
}
