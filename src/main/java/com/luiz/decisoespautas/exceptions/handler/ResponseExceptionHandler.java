package com.luiz.decisoespautas.exceptions.handler;

import com.luiz.decisoespautas.exceptions.ExceptionResponse;
import com.sun.jdi.request.DuplicateRequestException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
@RestController
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        logger.error(e);
        ExceptionResponse exception = new ExceptionResponse("Erro interno");
        return new ResponseEntity<>(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DuplicateRequestException.class)
    public ResponseEntity<ExceptionResponse> handleException(DuplicateRequestException e) {
        ExceptionResponse exception = new ExceptionResponse(e.getMessage());
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleException(IllegalArgumentException e) {
        ExceptionResponse exception = new ExceptionResponse(e.getMessage());
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(EntityNotFoundException e) {
        ExceptionResponse exception = new ExceptionResponse(e.getMessage());
        return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
    }

}
