package com.example.booking.exception;

public class InsufficientMoneyBalanceException extends RuntimeException{

    public InsufficientMoneyBalanceException(String message) {
        super(message);
    }
}
