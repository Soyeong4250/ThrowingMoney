package com.kakaopay.throwmoney.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorRes {

    private ErrorCode errorCode;
    private String message;

}
