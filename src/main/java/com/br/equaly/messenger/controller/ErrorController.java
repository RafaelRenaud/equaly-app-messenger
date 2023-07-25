package com.br.equaly.messenger.controller;

import com.br.equaly.messenger.exception.InvalidTemplateException;
import com.br.equaly.messenger.exception.InvalidTokenException;
import com.br.equaly.messenger.model.dto.error.ErrorHandlerDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.CharConversionException;
import java.net.ConnectException;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler({LockedException.class, DisabledException.class})
    public ResponseEntity authenticationError(Exception e){
        return ResponseEntity
                .status(HttpStatusCode.valueOf(HttpStatus.UNAUTHORIZED.value()))
                .body(new ErrorHandlerDTO(HttpStatus.UNAUTHORIZED, e));
    }

    @ExceptionHandler({CharConversionException.class, InvalidTokenException.class, InvalidTemplateException.class})
    public ResponseEntity badRequest(Exception e){
        return ResponseEntity
                .status(HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()))
                .body(new ErrorHandlerDTO(HttpStatus.BAD_REQUEST, e));
    }

    @ExceptionHandler({ConnectException.class})
    public ResponseEntity serviceUnavailable(){
        return ResponseEntity
                .status(HttpStatusCode.valueOf(HttpStatus.SERVICE_UNAVAILABLE.value()))
                .build();
    }

}
