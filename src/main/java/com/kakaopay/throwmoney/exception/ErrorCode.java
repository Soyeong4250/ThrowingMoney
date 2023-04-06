package com.kakaopay.throwmoney.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATE_USER(HttpStatus.CONFLICT, "이미 존재하는 회원입니다."),
    USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅방을 찾을 수 없습니다."),
    MONEY_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "거래정보를 찾을 수 없습니다."),
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    LACK_OF_MONEY(HttpStatus.BAD_REQUEST, "현재 보유하고 있는 금액이 부족합니다."),
    EXCESS_OF_PEOPLE(HttpStatus.BAD_REQUEST, "현재 인원보다 뿌리고자 하는 인원이 많습니다.");

    private HttpStatus status;
    private String message;

}