package com.example.booking.dto;

import com.example.booking.entity.RoomClass;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Data
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomsFilterSearchDto {
    private RoomClass roomClass;
    private Instant dateIn;
    private Instant dateOut;
}
