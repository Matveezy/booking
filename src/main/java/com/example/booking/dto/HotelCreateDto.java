package com.example.booking.dto;

import com.example.booking.entity.HotelClass;
import com.example.booking.validation.EnumNamePattern;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelCreateDto {
    @NotBlank(message = "hotel name cannot be blank")
    private String name;
    @NotBlank(message = "city name cannot be blank")
    private String city;
    @NotNull
    @EnumNamePattern(regexp = "ONE_STAR|TWO_STARS|THREE_STARS|FOUR_STARS|FIVE_STARS",
            message = "hotelClass can take the following values: ONE_STAR, TWO_STARS, THREE_STARS, FOUR_STARS, FIVE_STARS")
    private HotelClass hotelClass;
    @Min(value = 1, message = "ownerId value cannot be less than 1")
    private long ownerId;
}
