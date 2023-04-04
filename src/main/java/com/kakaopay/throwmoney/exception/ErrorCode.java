package com.kakaopay.throwmoney.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅방을 찾을 수 없습니다."),
    MONEY_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "기록을 찾을 수 없습니다.");

    private HttpStatus status;
    private String message;

}