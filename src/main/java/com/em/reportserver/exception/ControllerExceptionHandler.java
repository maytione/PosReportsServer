package com.em.reportserver.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
class ControllerExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    ErrorResult handleRuntimeException(RuntimeException e) {
        ErrorResult errorResult = new ErrorResult();
        errorResult.getFieldErrors().add(new FieldValidationError("Exception", e.getMessage()));
        return errorResult;
    }

    @Getter
    @NoArgsConstructor
    static class ErrorResult {
        private final List<FieldValidationError> fieldErrors = new ArrayList<>();
    }

    @Getter
    @AllArgsConstructor
    static class FieldValidationError {
        private String field;
        private String message;
    }

}