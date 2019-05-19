package com.aperez.exception;

public class InsufficientDrinkStock extends RuntimeException {
    private String message;

    public InsufficientDrinkStock(String errorMessage){
        this.message=errorMessage;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
