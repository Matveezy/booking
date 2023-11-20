package com.example.booking.exception;

public class NotEnoughPermissionException extends RuntimeException {

    public NotEnoughPermissionException(String message) {
        super(message);
    }
}
