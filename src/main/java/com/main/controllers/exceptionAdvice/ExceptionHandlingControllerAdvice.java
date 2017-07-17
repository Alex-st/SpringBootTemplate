package com.main.controllers.exceptionAdvice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * Created by alex on 7/14/2017.
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandlingControllerAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public String handleException(Exception e) {
        return processException("Unexpected internal service exception: %s.", e);
    }

    private String processException(String messageFormat, Exception e) {
        String errorMessage = format(messageFormat, e.getMessage());
        log.error(errorMessage, e);
        return errorMessage;
    }

}
