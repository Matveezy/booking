package com.example.booking.dto;

import com.example.booking.entity.RoomClass;
import com.example.booking.validation.DatesValid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Data
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DatesValid
public class RoomsFilterSearchDto implements DateInOutDto{
    private RoomClass roomClass;
    private Instant dateIn;
    private Instant dateOut;

    @Override
    public Instant in() {
        return dateIn;
    }

    @Override
    public Instant out() {
        return dateOut;
    }
}
