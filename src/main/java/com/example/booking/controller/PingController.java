package com.example.booking.controller;

import com.example.booking.dto.PingDto;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @PostMapping("/ping")
    public PingDto ping(@Validated @RequestBody PingDto msg) {
        return PingDto.builder()
                .data(msg.getData())
                .build();
    }

}

