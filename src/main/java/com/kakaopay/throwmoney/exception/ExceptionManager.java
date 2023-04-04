package com.kakaopay.throwmoney.exception;

import com.kakaopay.throwmoney.domain.dto.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(ApplicationException.class)
    public Response<ErrorCode> ApplicationExceptionHandler(ApplicationException e) {
        return Response.error(e.getErrorCode());
    }
}
