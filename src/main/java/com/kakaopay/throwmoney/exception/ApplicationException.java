package com.kakaopay.throwmoney.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplicationException extends RuntimeException {

    private ErrorCode errorCode;
    private String message;

    @Override
    public String toString() {
        if (message.equals(null)) return errorCode.getMessage();
        return  String.format("%s %s", errorCode.getMessage(), message);
    }
}
