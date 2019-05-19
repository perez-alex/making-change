package com.aperez.exception;

public class NotEnoughChangeException extends RuntimeException {
    private String message;

    public NotEnoughChangeException(String errorMessage) {
        this.message = errorMessage;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
