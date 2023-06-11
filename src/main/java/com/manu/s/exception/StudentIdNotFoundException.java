package com.manu.s.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StudentIdNotFoundException extends RuntimeException {
    public StudentIdNotFoundException(String message)
    {
        super(message);
    }
}
