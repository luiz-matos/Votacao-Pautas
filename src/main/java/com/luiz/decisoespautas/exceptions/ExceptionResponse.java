package com.luiz.decisoespautas.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;
import java.io.Serializable;

@Getter
@AllArgsConstructor
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExceptionResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String message;
}
