package com.winner_cat.global.enums.statuscode;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ErrorStatus implements BaseCode {
	// common
	_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
	_BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
	_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
	_FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

	// Member Error
	MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "해당하는 사용자를 찾을 수 없습니다."),
	PASSWORD_NOT_CORRECT(HttpStatus.FORBIDDEN, "MEMBER4002", "비밀번호가 일치하지 않습니다."),

	// Resource Error
	RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "RESOURCE4001", "잘못된 api 요청입니다. 반복적인 오류 발생시 관리자에게 문의해주세요."),

	// 로그인 실패 사유
	INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "AUTH4001", "아이디 또는 비밀번호가 잘못되었습니다."),
	ACCOUNT_LOCKED(HttpStatus.LOCKED, "AUTH4002", "계정이 잠겼습니다."),
	ACCOUNT_DISABLED(HttpStatus.FORBIDDEN, "AUTH4003", "계정이 비활성화되었습니다."),
	ACCOUNT_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH4004", "자격증명이 만료되었습니다."),

	// Article
	ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "ARTICLE4001", "삭제되었거나 존재하지 않는 게시글입니다."),
	ARTICLE_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "ARTICLE4002", "게시글 작성자만 접근 권한이 있습니다."),

	// Tag
	TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "TAG4001", "해당하는 태그가 없습니다"),

	// JWT Error
	TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "TOKEN4001", "토큰이 없거나 만료되었습니다."),
	TOKEN_NO_AUTHORIZATION(HttpStatus.UNAUTHORIZED, "TOKEN4002", "해당 토큰에 권한이 없습니다."),

	// member
	EXSISTS_MEMBER(HttpStatus.FORBIDDEN, "MEMBER4001", "이미 사용중인 이메일입니다."),


	// Question
	QUESTION_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "QUESTIONROOM4001", "존재하지 않는 질문방 입니다."),
	FAIL_TO_CREATE_ANSWER(HttpStatus.NOT_FOUND, "QUESTIONROOM4002", "답변을 생성하는데 실패하였습니다. GPT API키가 만료되었을 수 있습니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;


	// implement of BaseCode
	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	@Override
	public Integer getStatusValue() {
		return httpStatus.value();
	}
}
