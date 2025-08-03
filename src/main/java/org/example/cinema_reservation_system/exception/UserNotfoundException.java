package org.example.cinema_reservation_system.exception;

import lombok.Getter;

@Getter
public class UserNotfoundException extends RuntimeException {
    private final String code;

    public UserNotfoundException(String code, String message) {
        super(message);
        this.code = code;
    }
}