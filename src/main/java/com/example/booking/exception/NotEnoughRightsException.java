package com.example.booking.exception;

public class NotEnoughRightsException extends RuntimeException {
    public NotEnoughRightsException() {
        super("Not enough rights for this request");
    }
}
