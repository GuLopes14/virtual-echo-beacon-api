package br.com.challenge.ride_echo_beacon_api.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ValidationHandler {   
    record ErrorMessage(String message) {}

    record ValidationErrorMessage(String field, String message) {
        public ValidationErrorMessage(FieldError fieldError) {
            this(fieldError.getField(), fieldError.getDefaultMessage());
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public List<ValidationErrorMessage> handle(MethodArgumentNotValidException e) {
        return e.getFieldErrors()
                .stream()
                .map(ValidationErrorMessage::new) 
                .toList();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handle(Exception e) {
        return new ErrorMessage("Erro interno no servidor: " + e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorMessage handle(HttpMessageNotReadableException e) {
        return new ErrorMessage("Erro no formato da requisição: " + e.getMostSpecificCause().getMessage());
    }

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus
    public ErrorMessage handle(ResponseStatusException e) {
        return new ErrorMessage(e.getReason());
    }
}