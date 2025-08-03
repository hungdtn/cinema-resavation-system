package org.example.cinema_reservation_system.exception;

import lombok.Getter;

@Getter
public class UserNameOrPasswordInvalidException extends RuntimeException {
    private final String code;

    public UserNameOrPasswordInvalidException(String code, String message) {
        super(message);
        this.code = code;
    }
}