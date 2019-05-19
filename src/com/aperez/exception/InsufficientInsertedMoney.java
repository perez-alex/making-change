package com.aperez.exception;

public class InsufficientInsertedMoney extends RuntimeException {
    private String message;

    public InsufficientInsertedMoney(String errorMessage) {
        this.message = errorMessage;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
