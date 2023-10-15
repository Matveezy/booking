package com.example.booking.exception;

public class HotelNotFoundException extends RuntimeException {
    public HotelNotFoundException(long id) {
        super("Hotel with id=" + id + " not found");
    }
}
