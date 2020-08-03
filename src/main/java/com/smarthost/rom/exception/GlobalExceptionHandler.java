package com.smarthost.rom.exception;

import com.smarthost.rom.dto.ErrorResponse;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                               HttpStatus status, WebRequest request) {
        var badRequest = HttpStatus.BAD_REQUEST;
        return super.handleExceptionInternal(ex, new ErrorResponse(ex.getMessage(), badRequest.value()), headers,
                badRequest, request);
    }

    @Override
    public ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status,
                                                     WebRequest request) {
        if (ex instanceof MethodArgumentTypeMismatchException) {
            var exception = (MethodArgumentTypeMismatchException) ex;
            var badRequest = HttpStatus.BAD_REQUEST;
            var message = String.format("Value '%s' is invalid for field %s", exception.getValue(), exception.getName());
            return super.handleExceptionInternal(exception, new ErrorResponse(message, badRequest.value()), new HttpHeaders(),
                    badRequest, request);
        }

        return super.handleTypeMismatch(ex, headers, status, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        var badRequest = HttpStatus.BAD_REQUEST;
        String message = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));
        return super.handleExceptionInternal(ex, new ErrorResponse(message, badRequest.value()), new HttpHeaders(), badRequest, request);
    }
}
