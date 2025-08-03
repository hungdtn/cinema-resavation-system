package org.example.cinema_reservation_system.exception;

import lombok.Getter;

@Getter
public class InputInvalidException extends RuntimeException {
    private final String code;

    public InputInvalidException(String code, String message) {
        super(message);
        this.code = code;
    }

}
