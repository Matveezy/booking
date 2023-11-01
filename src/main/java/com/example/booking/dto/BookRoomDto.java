package com.example.booking.dto;

import lombok.*;

import java.time.Instant;

@Data
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookRoomDto {
    private long userId; //todo get user id by jwt
    private Instant dateIn;
    private Instant dateOut;
}
