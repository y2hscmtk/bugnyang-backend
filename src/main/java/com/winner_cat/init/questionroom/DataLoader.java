package com.winner_cat.init.questionroom;

import com.winner_cat.domain.questionroom.entity.Answer;
import com.winner_cat.domain.questionroom.entity.Question;
import com.winner_cat.domain.questionroom.entity.QuestionRoom;
import com.winner_cat.domain.questionroom.entity.enums.QuestionState;
import com.winner_cat.domain.questionroom.repository.AnswerRepository;
import com.winner_cat.domain.questionroom.repository.QuestionRepository;
import com.winner_cat.domain.questionroom.repository.QuestionRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final QuestionRoomRepository questionRoomRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Override
    public void run(String... args) throws Exception {
        // 질문방 생성
        QuestionRoom room1 = questionRoomRepository.save(
                QuestionRoom.builder().state(QuestionState.PROGRESS).build()
        );
        QuestionRoom room2 = questionRoomRepository.save(
                QuestionRoom.builder().state(QuestionState.PROGRESS).build()
        );

        // 질문과 답변 생성 (room1)
        addQuestionsAndAnswersForRoom1(room1);

        // 질문과 답변 생성 (room2)
        addQuestionsAndAnswersForRoom2(room2);
    }

    private void addQuestionsAndAnswersForRoom1(QuestionRoom room1) {
        Question q1 = Question.builder()
                .content("프로그래밍 언어 중 가장 인기 있는 것은 무엇인가요?")
                .questionRoom(room1)
                .build();
        q1.setCreatedAt(LocalDateTime.now().minusHours(8));
        questionRepository.save(q1);

        Answer a1 = Answer.builder()
                .content("가장 인기 있는 프로그래밍 언어는 현재 파이썬입니다.")
                .questionRoom(room1)
                .build();
        a1.setCreatedAt(LocalDateTime.now().minusHours(7).minusMinutes(30));
        answerRepository.save(a1);

        Question q2 = Question.builder()
                .content("Java와 Python 중 어느 것이 더 좋은가요?")
                .questionRoom(room1)
                .build();
        q2.setCreatedAt(LocalDateTime.now().minusHours(6));
        questionRepository.save(q2);

        Answer a2 = Answer.builder()
                .content("각 언어는 용도에 따라 다릅니다. Java는 대규모 시스템에 적합하고 Python은 데이터 분석과 AI에 적합합니다.")
                .questionRoom(room1)
                .build();
        a2.setCreatedAt(LocalDateTime.now().minusHours(5).minusMinutes(45));
        answerRepository.save(a2);

        Question q3 = Question.builder()
                .content("Python이 데이터 분석에 적합한 이유는 무엇인가요?")
                .questionRoom(room1)
                .build();
        q3.setCreatedAt(LocalDateTime.now().minusHours(5));
        questionRepository.save(q3);

        Answer a3 = Answer.builder()
                .content("Python은 다양한 데이터 분석 라이브러리와 툴이 잘 갖춰져 있기 때문에 데이터 분석에 적합합니다.")
                .questionRoom(room1)
                .build();
        a3.setCreatedAt(LocalDateTime.now().minusHours(4).minusMinutes(30));
        answerRepository.save(a3);

        Question q4 = Question.builder()
                .content("Python의 주요 데이터 분석 라이브러리에는 어떤 것들이 있나요?")
                .questionRoom(room1)
                .build();
        q4.setCreatedAt(LocalDateTime.now().minusHours(4));
        questionRepository.save(q4);

        Answer a4 = Answer.builder()
                .content("주요 라이브러리로는 Pandas, NumPy, Matplotlib, Scikit-learn 등이 있습니다.")
                .questionRoom(room1)
                .build();
        a4.setCreatedAt(LocalDateTime.now().minusHours(3).minusMinutes(30));
        answerRepository.save(a4);

        Question q5 = Question.builder()
                .content("Pandas 라이브러리는 어떤 역할을 하나요?")
                .questionRoom(room1)
                .build();
        q5.setCreatedAt(LocalDateTime.now().minusHours(3));
        questionRepository.save(q5);

        Answer a5 = Answer.builder()
                .content("Pandas는 데이터 조작과 분석을 쉽게 할 수 있도록 도와주는 라이브러리입니다.")
                .questionRoom(room1)
                .build();
        a5.setCreatedAt(LocalDateTime.now().minusHours(2).minusMinutes(30));
        answerRepository.save(a5);

        Question q6 = Question.builder()
                .content("NumPy와 Pandas의 차이점은 무엇인가요?")
                .questionRoom(room1)
                .build();
        q6.setCreatedAt(LocalDateTime.now().minusHours(2));
        questionRepository.save(q6);

        Answer a6 = Answer.builder()
                .content("NumPy는 주로 수치 계산에, Pandas는 데이터 조작과 분석에 특화된 라이브러리입니다.")
                .questionRoom(room1)
                .build();
        a6.setCreatedAt(LocalDateTime.now().minusHours(1).minusMinutes(30));
        answerRepository.save(a6);

        Question q7 = Question.builder()
                .content("Matplotlib을 사용하여 그래프를 그리는 방법은 무엇인가요?")
                .questionRoom(room1)
                .build();
        q7.setCreatedAt(LocalDateTime.now().minusHours(1));
        questionRepository.save(q7);

        Answer a7 = Answer.builder()
                .content("Matplotlib의 pyplot 모듈을 사용하여 다양한 종류의 그래프를 그릴 수 있습니다.")
                .questionRoom(room1)
                .build();
        a7.setCreatedAt(LocalDateTime.now().minusMinutes(30));
        answerRepository.save(a7);
    }

    private void addQuestionsAndAnswersForRoom2(QuestionRoom room2) {
        Question q1 = Question.builder()
                .content("Spring Boot와 Django 중 어떤 프레임워크를 사용해야 하나요?")
                .questionRoom(room2)
                .build();
        q1.setCreatedAt(LocalDateTime.now().minusHours(8));
        questionRepository.save(q1);

        Answer a1 = Answer.builder()
                .content("Spring Boot는 Java 기반의 웹 프레임워크이며, Django는 Python 기반의 웹 프레임워크입니다. 프로젝트의 요구사항에 따라 선택하세요.")
                .questionRoom(room2)
                .build();
        a1.setCreatedAt(LocalDateTime.now().minusHours(7).minusMinutes(30));
        answerRepository.save(a1);

        Question q2 = Question.builder()
                .content("Spring Boot와 Django의 주요 차이점은 무엇인가요?")
                .questionRoom(room2)
                .build();
        q2.setCreatedAt(LocalDateTime.now().minusHours(6));
        questionRepository.save(q2);

        Answer a2 = Answer.builder()
                .content("Spring Boot는 Java 기반으로 대규모 시스템에 적합하고, Django는 Python 기반으로 빠른 개발에 적합합니다.")
                .questionRoom(room2)
                .build();
        a2.setCreatedAt(LocalDateTime.now().minusHours(5).minusMinutes(45));
        answerRepository.save(a2);

        Question q3 = Question.builder()
                .content("Spring Boot의 장점은 무엇인가요?")
                .questionRoom(room2)
                .build();
        q3.setCreatedAt(LocalDateTime.now().minusHours(5));
        questionRepository.save(q3);

        Answer a3 = Answer.builder()
                .content("Spring Boot는 설정이 간편하고, 마이크로서비스 아키텍처에 적합하며, 다양한 플러그인과 라이브러리를 제공합니다.")
                .questionRoom(room2)
                .build();
        a3.setCreatedAt(LocalDateTime.now().minusHours(4).minusMinutes(30));
        answerRepository.save(a3);

        Question q4 = Question.builder()
                .content("Django의 장점은 무엇인가요?")
                .questionRoom(room2)
                .build();
        q4.setCreatedAt(LocalDateTime.now().minusHours(4));
        questionRepository.save(q4);

        Answer a4 = Answer.builder()
                .content("Django는 빠른 개발을 위해 많은 기능을 내장하고 있으며, 보안에 강하고, 관리 인터페이스를 기본 제공합니다.")
                .questionRoom(room2)
                .build();
        a4.setCreatedAt(LocalDateTime.now().minusHours(3).minusMinutes(30));
        answerRepository.save(a4);

        Question q5 = Question.builder()
                .content("Spring Boot와 Django 중 어느 것이 더 배우기 쉬운가요?")
                .questionRoom(room2)
                .build();
        q5.setCreatedAt(LocalDateTime.now().minusHours(3));
        questionRepository.save(q5);

        Answer a5 = Answer.builder()
                .content("배우기 쉬운 것은 개인의 배경에 따라 다릅니다. Java에 익숙하다면 Spring Boot가, Python에 익숙하다면 Django가 배우기 쉽습니다.")
                .questionRoom(room2)
                .build();
        a5.setCreatedAt(LocalDateTime.now().minusHours(2).minusMinutes(30));
        answerRepository.save(a5);

        Question q6 = Question.builder()
                .content("Spring Boot로 REST API를 만드는 방법은 무엇인가요?")
                .questionRoom(room2)
                .build();
        q6.setCreatedAt(LocalDateTime.now().minusHours(2));
        questionRepository.save(q6);

        Answer a6 = Answer.builder()
                .content("Spring Boot에서 @RestController와 @RequestMapping을 사용하여 REST API를 쉽게 만들 수 있습니다.")
                .questionRoom(room2)
                .build();
        a6.setCreatedAt(LocalDateTime.now().minusHours(1).minusMinutes(30));
        answerRepository.save(a6);

        Question q7 = Question.builder()
                .content("Django에서 REST API를 만드는 방법은 무엇인가요?")
                .questionRoom(room2)
                .build();
        q7.setCreatedAt(LocalDateTime.now().minusHours(1));
        questionRepository.save(q7);

        Answer a7 = Answer.builder()
                .content("Django에서 Django REST framework를 사용하여 REST API를 쉽게 만들 수 있습니다.")
                .questionRoom(room2)
                .build();
        a7.setCreatedAt(LocalDateTime.now().minusMinutes(30));
        answerRepository.save(a7);
    }
}
