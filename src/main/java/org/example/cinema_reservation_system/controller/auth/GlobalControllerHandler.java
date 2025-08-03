package org.example.cinema_reservation_system.controller.auth;

import org.example.cinema_reservation_system.dto.user.ResponseDto;
import org.example.cinema_reservation_system.exception.InputInvalidException;
import org.example.cinema_reservation_system.exception.UserNameOrPasswordInvalidException;
import org.example.cinema_reservation_system.exception.UserNotfoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerHandler {

    @ExceptionHandler(UserNotfoundException.class)
    public ResponseEntity<ResponseDto<Void>> handleUserNotFoundException(UserNotfoundException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseDto.fail(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(InputInvalidException.class)
    public ResponseEntity<ResponseDto<Void>> handleInvalidRequestException(InputInvalidException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseDto.fail(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(UserNameOrPasswordInvalidException.class)
    public ResponseEntity<ResponseDto<Void>> userNameOrPasswordInvalidException(UserNameOrPasswordInvalidException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseDto.fail(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<Void>> userNameOrPasswordInvalidException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseDto.fail("500", ex.getMessage()));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }
}
