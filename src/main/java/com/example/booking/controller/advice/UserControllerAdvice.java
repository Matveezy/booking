package com.example.booking.controller.advice;

import com.example.booking.exception.HotelNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = { UserControllerAdvice.class })
public class UserControllerAdvice {

    @ExceptionHandler(HotelNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle(HotelNotFoundException e) {
        return e.getMessage();
    }
}
