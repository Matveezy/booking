package com.example.booking.dto;

import com.example.booking.entity.RoomClass;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomUpdateDto {
    @Pattern(regexp = "SINGLE|DOUBLE|TRIPLE|QUAD",
            message = "hotelClass can take the following values: SINGLE, DOUBLE, TRIPLE, QUAD")
    private RoomClass roomClass;
    private long roomNumber;
    @Min(value = 0, message = "price value cannot be less than 0")
    private long price;
}
