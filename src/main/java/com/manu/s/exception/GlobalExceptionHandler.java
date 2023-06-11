package com.manu.s.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity<ErrorDetails> handlingEmailAlreadyRegisteredException(EmailAlreadyRegisteredException ex,
                                                                                WebRequest request)
    {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setErrorTime(LocalDateTime.now());
        errorDetails.setMessage(ex.getMessage());
        errorDetails.setPath(request.getDescription(false));
        errorDetails.setErrorCode("EMAIL_ALREADY_REGISTERED");

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StudentIdNotFoundException.class)
    public ResponseEntity<ErrorDetails> handlingStudentIdNotFoundException(StudentIdNotFoundException ex,
                                                                                WebRequest request)
    {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setErrorTime(LocalDateTime.now());
        errorDetails.setMessage(ex.getMessage());
        errorDetails.setPath(request.getDescription(false));
        errorDetails.setErrorCode("STUDENT_ID_NOT_FOUND");

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

}
